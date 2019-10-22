import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILimit } from 'app/shared/model/limit.model';

type EntityResponseType = HttpResponse<ILimit>;
type EntityArrayResponseType = HttpResponse<ILimit[]>;

@Injectable({ providedIn: 'root' })
export class LimitService {
  public resourceUrl = SERVER_API_URL + 'api/limits';

  constructor(protected http: HttpClient) {}

  create(limit: ILimit): Observable<EntityResponseType> {
    return this.http.post<ILimit>(this.resourceUrl, limit, { observe: 'response' });
  }

  update(limit: ILimit): Observable<EntityResponseType> {
    return this.http.put<ILimit>(this.resourceUrl, limit, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILimit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILimit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
