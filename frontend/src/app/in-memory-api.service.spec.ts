import { TestBed } from '@angular/core/testing';

import { InMemoryApiService } from './in-memory-api.service';

describe('InMemoryApiService', () => {
  let service: InMemoryApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InMemoryApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
