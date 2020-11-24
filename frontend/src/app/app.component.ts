import { Component } from '@angular/core';
import { Image } from './image';
import { ImageService } from './image.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';

  images: Image[] = [];

  constructor(private imageService: ImageService) {
  }

  ngOnInit() {
    this.getImages();
  }

  getImages(): void {
    this.imageService.getAll()
      .subscribe(images => this.images = images);
  }
}
