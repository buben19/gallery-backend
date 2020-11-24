import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class InMemoryImageService {

  constructor() { }

  createDb() {
      let images = [
        { title: 'Windstorm' },
        { title: 'Bombasto' },
        { title: 'Magneta' },
        { title: 'Tornado' }
      ];
      return {images};
    }
}
