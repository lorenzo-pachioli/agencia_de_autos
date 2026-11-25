import { Component, effect, input, model, output, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, Subject, catchError, debounceTime, of, switchMap } from 'rxjs';

const ESPERA_BUSQUEDA_MS = 250;

/** Campo con lista desplegable que se filtra según lo escrito. Solo permite elegir de la lista. */
@Component({
  selector: 'app-buscador',
  imports: [ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule],
  templateUrl: './buscador.component.html',
  styles: ':host { display: block; }',
})
export class BuscadorComponent<T extends object> {
  readonly etiqueta = input.required<string>();
  /** Devuelve las coincidencias para lo escrito. Con texto vacío, las primeras. */
  readonly buscar = input.required<(texto: string) => Observable<T[]>>();
  readonly describir = input.required<(opcion: T) => string>();
  readonly error = input('');
  readonly seleccionado = model<T | null>(null);
  readonly escrito = output<string>();

  protected readonly control = new FormControl<T | string>('', { nonNullable: true });
  protected readonly opciones = signal<T[]>([]);
  protected readonly estado = signal<'buscando' | 'listo' | 'fallo'>('buscando');
  protected readonly estadoDeError: ErrorStateMatcher = { isErrorState: () => !!this.error() };
  private readonly consultas = new Subject<string>();

  constructor() {
    this.consultas
      .pipe(
        debounceTime(ESPERA_BUSQUEDA_MS),
        switchMap(texto => this.buscar()(texto).pipe(catchError(() => of(null)))),
        takeUntilDestroyed(),
      )
      .subscribe(resultado => {
        this.opciones.set(resultado ?? []);
        this.estado.set(resultado ? 'listo' : 'fallo');
      });

    this.control.valueChanges.pipe(takeUntilDestroyed()).subscribe(valor => {
      if (typeof valor !== 'string') {
        this.seleccionado.set(valor);
        return;
      }
      this.seleccionado.set(null);
      this.escrito.emit(valor);
      this.consultar(valor);
    });

    // Muestra en el campo una selección puesta desde afuera, como el vendedor preseleccionado
    effect(() => {
      const elegido = this.seleccionado();
      if (elegido && elegido !== this.control.value) this.control.setValue(elegido, { emitEvent: false });
    });
  }

  protected alEnfocar(): void {
    const valor = this.control.value;
    this.consultar(typeof valor === 'string' ? valor : '');
  }

  protected readonly mostrar = (valor: T | string | null): string => {
    if (!valor) return '';
    return typeof valor === 'string' ? valor : this.describir()(valor);
  };

  private consultar(texto: string): void {
    this.estado.set('buscando');
    this.consultas.next(texto.trim());
  }
}
