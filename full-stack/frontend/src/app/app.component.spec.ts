import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { AppComponent } from './app.component';
import { CaseService } from './case.service';

describe('case details setup', () => {
  const caseItem = { id: 'case-001', reference: 'TEST-001', status: 'OPEN' as const };

  let service: {
    getCase: ReturnType<typeof vi.fn>;
    closeCase: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    service = {
      getCase: vi.fn().mockReturnValue(of(caseItem)),
      closeCase: vi.fn().mockReturnValue(of({ ...caseItem, status: 'CLOSED' })),
    };

    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [{ provide: CaseService, useValue: service }],
    });
  });

  it('loads and displays the supplied case', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    expect(service.getCase).toHaveBeenCalledWith('case-001');
    expect(fixture.nativeElement.textContent).toContain('TEST-001');
    expect(fixture.nativeElement.querySelector('[data-testid="case-status"]').textContent).toBe('OPEN');
  });

  it('shows a load error when the backend request fails', () => {
    service.getCase.mockReturnValue(throwError(() => new Error('Failed to load case')));

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Cannot load the case. Check the backend is running on port 8080.');
  });

  it('closes the active case and updates the task message', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');
    button.click();
    fixture.detectChanges();

    expect(service.closeCase).toHaveBeenCalledWith('case-001');
    expect(fixture.nativeElement.textContent).toContain('The case has been closed.');
  });

  it('does not close a case that is already closed', () => {
    service.getCase.mockReturnValue(of({ ...caseItem, status: 'CLOSED' }));

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');
    expect(button.disabled).toBe(true);

    button.click();
    fixture.detectChanges();

    expect(service.closeCase).not.toHaveBeenCalled();
  });
});
