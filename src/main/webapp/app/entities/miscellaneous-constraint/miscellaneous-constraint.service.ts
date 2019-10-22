import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';

type EntityResponseType = HttpResponse<IMiscellaneousConstraint>;
type EntityArrayResponseType = HttpResponse<IMiscellaneousConstraint[]>;

@Injectable({ providedIn: 'root' })
export class MiscellaneousConstraintService {
  public resourceUrl = SERVER_API_URL + 'api/miscellaneous-constraints';

  constructor(protected http: HttpClient) {}

  create(miscellaneousConstraint: IMiscellaneousConstraint): Observable<EntityResponseType> {
    return this.http.post<IMiscellaneousConstraint>(this.resourceUrl, miscellaneousConstraint, { observe: 'response' });
  }

  update(miscellaneousConstraint: IMiscellaneousConstraint): Observable<EntityResponseType> {
    return this.http.put<IMiscellaneousConstraint>(this.resourceUrl, miscellaneousConstraint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMiscellaneousConstraint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMiscellaneousConstraint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
