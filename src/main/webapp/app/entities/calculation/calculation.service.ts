import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICalculation } from 'app/shared/model/calculation.model';

type EntityResponseType = HttpResponse<ICalculation>;
type EntityArrayResponseType = HttpResponse<ICalculation[]>;

@Injectable({ providedIn: 'root' })
export class CalculationService {
  public resourceUrl = SERVER_API_URL + 'api/calculations';

  constructor(protected http: HttpClient) {}

  create(calculation: ICalculation): Observable<EntityResponseType> {
    return this.http.post<ICalculation>(this.resourceUrl, calculation, { observe: 'response' });
  }

  update(calculation: ICalculation): Observable<EntityResponseType> {
    return this.http.put<ICalculation>(this.resourceUrl, calculation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICalculation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICalculation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
