import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface CaseItem { id: string; reference: string; status: 'OPEN' | 'CLOSED'; }
@Injectable({ providedIn: 'root' })
export class CaseService {
 private readonly http = inject(HttpClient);
 getCase(id: string): Observable<CaseItem> { return this.http.get<CaseItem>(`/api/cases/${encodeURIComponent(id)}`); }
 closeCase(id: string): Observable<CaseItem> { return this.http.post<CaseItem>(`/api/cases/${encodeURIComponent(id)}/close`, {}); }
}
