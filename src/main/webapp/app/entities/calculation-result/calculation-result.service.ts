import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICalculationResult } from 'app/shared/model/calculation-result.model';

type EntityResponseType = HttpResponse<ICalculationResult>;
type EntityArrayResponseType = HttpResponse<ICalculationResult[]>;

@Injectable({ providedIn: 'root' })
export class CalculationResultService {
  public resourceUrl = SERVER_API_URL + 'api/calculation-results';

  constructor(protected http: HttpClient) {}

  create(calculationResult: ICalculationResult): Observable<EntityResponseType> {
    return this.http.post<ICalculationResult>(this.resourceUrl, calculationResult, { observe: 'response' });
  }

  update(calculationResult: ICalculationResult): Observable<EntityResponseType> {
    return this.http.put<ICalculationResult>(this.resourceUrl, calculationResult, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICalculationResult>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICalculationResult[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
