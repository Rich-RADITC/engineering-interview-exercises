import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { CaseItem, CaseService } from './case.service';
@Component({ selector: 'app-root', standalone: true, changeDetection: ChangeDetectionStrategy.Default, templateUrl: './app.component.html' })
export class AppComponent implements OnInit {
 private readonly caseService = inject(CaseService);
 caseItem?: CaseItem;
 loadError = '';
 taskMessage = '';
 errorMessage = '';
 submitting = false;
 ngOnInit(): void {
  this.caseService.getCase('case-001').subscribe({
   next: item => { this.caseItem = item; },
   error: () => { this.loadError = 'Cannot load the case. Check the backend is running on port 8080.'; }
  });
 }
 closeCase(): void {
  const current = this.caseItem;
  if (!current || current.status !== 'OPEN' || this.submitting) { return; }
  this.submitting = true;
  this.errorMessage = '';
  this.taskMessage = 'Closing case...';
  this.caseService.closeCase(current.id).subscribe({
   next: item => {
    this.caseItem = item;
    this.submitting = false;
    this.taskMessage = 'Case closed.';
   },
   error: (err: HttpErrorResponse) => {
    this.submitting = false;
    this.taskMessage = '';
    this.errorMessage = err.status === 409
     ? 'This case cannot be closed because it is already closed.'
     : err.status === 404
      ? 'This case no longer exists.'
      : 'Could not close the case. Please try again.';
   }
  });
 }
}
