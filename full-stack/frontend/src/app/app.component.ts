import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { CaseItem, CaseService } from './case.service';
@Component({ selector: 'app-root', standalone: true, changeDetection: ChangeDetectionStrategy.Default, templateUrl: './app.component.html' })
export class AppComponent implements OnInit {
 private readonly caseService = inject(CaseService);
 caseItem?: CaseItem;
 loadError = '';
 taskMessage = '';
 ngOnInit(): void {
  this.caseService.getCase('case-001').subscribe({
   next: item => { this.caseItem = item; },
   error: () => { this.loadError = 'Cannot load the case. Check the backend is running on port 8080.'; }
  });
 }
 closeCase(): void {
  // TODO: implement the interaction described in full-stack/BRIEF.md.
  this.taskMessage = 'The Close case action is your interview task.';
 }
}
