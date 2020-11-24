import { TestBed } from '@angular/core/testing';

import { InMemoryImageService } from './in-memory-image.service';

describe('InMemoryImageService', () => {
  let service: InMemoryImageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InMemoryImageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
