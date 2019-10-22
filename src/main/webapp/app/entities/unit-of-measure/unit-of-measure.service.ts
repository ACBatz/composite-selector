import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';

type EntityResponseType = HttpResponse<IUnitOfMeasure>;
type EntityArrayResponseType = HttpResponse<IUnitOfMeasure[]>;

@Injectable({ providedIn: 'root' })
export class UnitOfMeasureService {
  public resourceUrl = SERVER_API_URL + 'api/unit-of-measures';

  constructor(protected http: HttpClient) {}

  create(unitOfMeasure: IUnitOfMeasure): Observable<EntityResponseType> {
    return this.http.post<IUnitOfMeasure>(this.resourceUrl, unitOfMeasure, { observe: 'response' });
  }

  update(unitOfMeasure: IUnitOfMeasure): Observable<EntityResponseType> {
    return this.http.put<IUnitOfMeasure>(this.resourceUrl, unitOfMeasure, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUnitOfMeasure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUnitOfMeasure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
