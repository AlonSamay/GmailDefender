import { TestBed } from "@angular/core/testing";

import { LocalServerService } from "./local-server.service";

describe("LocalServerServiceService", () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it("should be created", () => {
    const service: LocalServerService = TestBed.get(LocalServerService);
    expect(service).toBeTruthy();
  });
});
