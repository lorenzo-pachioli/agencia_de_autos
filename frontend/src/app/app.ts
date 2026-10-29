import { Component, signal } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';

@Component({
  imports: [RouterOutlet,HeaderComponent,FooterComponent],
  providers: [provideHttpClient()],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('frontend');
}
