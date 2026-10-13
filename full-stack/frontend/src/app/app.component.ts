import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { CaseItem, CaseService } from './case.service';
@Component({ selector: 'app-root', standalone: true, changeDetection: ChangeDetectionStrategy.Default, templateUrl: './app.component.html' })
export class AppComponent implements OnInit {
 private readonly caseService = inject(CaseService);
 caseItem?: CaseItem;
 loadError = '';
 taskMessage = '';
 isClosing = false;
 ngOnInit(): void {
  this.caseService.getCase('case-001').subscribe({
   next: item => { this.caseItem = item; },
   error: () => { this.loadError = 'Cannot load the case. Check the backend is running on port 8080.'; }
  });
 }
 closeCase(): void {
    if(this.isClosing || !this.caseItem || this.caseItem.status !== 'OPEN') {
        return;
    }
    this.isClosing = true;
    this.taskMessage = 'Closing the case...';
    this.caseService.closeCase(this.caseItem.id).subscribe({
    next: item => { this.isClosing = true; this.taskMessage = 'The case has been closed.'; },
   error: () => { this.taskMessage = 'Failed to close the case.'; }
  });
}
}
  
