import { Injectable } from '@angular/core';
import { InMemoryDbService } from 'angular-in-memory-web-api';

@Injectable({
  providedIn: 'root'
})
export class InMemoryApiService implements InMemoryDbService {

  constructor() { }

  createDb() {
      let images = [
        { id: 1, title: 'Windstorm', owner: 1 },
        { id: 2, title: 'Bombasto', owner: 1 },
        { id: 3, title: 'Magneta', owner: 1 },
        { id: 4, title: 'Tornado', owner: 1 }
      ];
      return {images};
    }
}
