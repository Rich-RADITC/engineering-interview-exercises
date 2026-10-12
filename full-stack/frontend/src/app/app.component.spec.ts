import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { describe, it, expect, vi } from 'vitest';
import { AppComponent } from './app.component';
import { CaseService } from './case.service';
describe('case details setup', () => {
 it('loads and displays the supplied case', async () => {
  const service = { getCase: vi.fn().mockReturnValue(of({ id: 'case-001', reference: 'TEST-001', status: 'OPEN' })), closeCase: vi.fn() };
  await TestBed.configureTestingModule({ imports: [AppComponent], providers: [{ provide: CaseService, useValue: service }] }).compileComponents();
  const fixture = TestBed.createComponent(AppComponent);
  fixture.detectChanges();
  expect(service.getCase).toHaveBeenCalledWith('case-001');
  expect(fixture.nativeElement.textContent).toContain('TEST-001');
  expect(fixture.nativeElement.querySelector('[data-testid="case-status"]').textContent).toBe('OPEN');
 });
 // TODO: add tests for the Close case interaction.
});
