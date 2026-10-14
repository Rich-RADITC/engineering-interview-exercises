import { TestBed } from '@angular/core/testing';
import { Subject, of, throwError } from 'rxjs';
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
});
describe('close case interaction', () => {
 const open = { id: 'case-001', reference: 'TEST-001', status: 'OPEN' };
 async function setup(closeCase: unknown) {
  const service = { getCase: vi.fn().mockReturnValue(of(open)), closeCase: vi.fn().mockReturnValue(closeCase) };
  await TestBed.configureTestingModule({ imports: [AppComponent], providers: [{ provide: CaseService, useValue: service }] }).compileComponents();
  const fixture = TestBed.createComponent(AppComponent);
  fixture.detectChanges();
  const el = fixture.nativeElement as HTMLElement;
  return { service, fixture, el, button: () => el.querySelector('button') as HTMLButtonElement };
 }
 it('shows the returned case on success', async () => {
  const { service, fixture, el, button } = await setup(of({ ...open, status: 'CLOSED' }));
  button().click();
  fixture.detectChanges();
  expect(service.closeCase).toHaveBeenCalledWith('case-001');
  expect(el.querySelector('[data-testid="case-status"]')!.textContent).toBe('CLOSED');
  expect(button().disabled).toBe(true);
 });
 it('disables the button and sends one request while pending', async () => {
  const { service, fixture, button } = await setup(new Subject());
  button().click();
  fixture.detectChanges();
  expect(button().disabled).toBe(true);
  button().click();
  expect(service.closeCase).toHaveBeenCalledTimes(1);
 });
 it('keeps the case, shows an error and allows retry on failure', async () => {
  const { fixture, el, button } = await setup(throwError(() => ({ status: 500 })));
  button().click();
  fixture.detectChanges();
  expect(el.querySelector('[data-testid="case-status"]')!.textContent).toBe('OPEN');
  expect(el.querySelector('[data-testid="close-error"]')!.textContent).toContain('try again');
  expect(button().disabled).toBe(false);
 });
});
