import { HttpInterceptorFn, type HttpErrorResponse} from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).token();
  const router = inject(Router);

  if (!token || req.url.includes('/auth/')) {
    return next(req);
  }

  return next(req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })).pipe(
    catchError((error : HttpErrorResponse) => {
      if(error.status === 404) {
        router.navigate(['/not-found']);    
      }
      return throwError(()=>error);
    }
    )
  );
};
