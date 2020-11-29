import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Image } from './model/image'

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Image[]> {
    return this.http.get<Image[]>('api/images');
  }
}
