import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILoadDirection } from 'app/shared/model/load-direction.model';

type EntityResponseType = HttpResponse<ILoadDirection>;
type EntityArrayResponseType = HttpResponse<ILoadDirection[]>;

@Injectable({ providedIn: 'root' })
export class LoadDirectionService {
  public resourceUrl = SERVER_API_URL + 'api/load-directions';

  constructor(protected http: HttpClient) {}

  create(loadDirection: ILoadDirection): Observable<EntityResponseType> {
    return this.http.post<ILoadDirection>(this.resourceUrl, loadDirection, { observe: 'response' });
  }

  update(loadDirection: ILoadDirection): Observable<EntityResponseType> {
    return this.http.put<ILoadDirection>(this.resourceUrl, loadDirection, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoadDirection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoadDirection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
