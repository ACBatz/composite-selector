import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWeightingFactor } from 'app/shared/model/weighting-factor.model';

type EntityResponseType = HttpResponse<IWeightingFactor>;
type EntityArrayResponseType = HttpResponse<IWeightingFactor[]>;

@Injectable({ providedIn: 'root' })
export class WeightingFactorService {
  public resourceUrl = SERVER_API_URL + 'api/weighting-factors';

  constructor(protected http: HttpClient) {}

  create(weightingFactor: IWeightingFactor): Observable<EntityResponseType> {
    return this.http.post<IWeightingFactor>(this.resourceUrl, weightingFactor, { observe: 'response' });
  }

  update(weightingFactor: IWeightingFactor): Observable<EntityResponseType> {
    return this.http.put<IWeightingFactor>(this.resourceUrl, weightingFactor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWeightingFactor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWeightingFactor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
