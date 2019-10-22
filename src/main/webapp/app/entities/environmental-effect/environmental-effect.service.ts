import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEnvironmentalEffect } from 'app/shared/model/environmental-effect.model';

type EntityResponseType = HttpResponse<IEnvironmentalEffect>;
type EntityArrayResponseType = HttpResponse<IEnvironmentalEffect[]>;

@Injectable({ providedIn: 'root' })
export class EnvironmentalEffectService {
  public resourceUrl = SERVER_API_URL + 'api/environmental-effects';

  constructor(protected http: HttpClient) {}

  create(environmentalEffect: IEnvironmentalEffect): Observable<EntityResponseType> {
    return this.http.post<IEnvironmentalEffect>(this.resourceUrl, environmentalEffect, { observe: 'response' });
  }

  update(environmentalEffect: IEnvironmentalEffect): Observable<EntityResponseType> {
    return this.http.put<IEnvironmentalEffect>(this.resourceUrl, environmentalEffect, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnvironmentalEffect>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnvironmentalEffect[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
