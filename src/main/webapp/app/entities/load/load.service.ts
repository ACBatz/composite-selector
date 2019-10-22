import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILoad } from 'app/shared/model/load.model';

type EntityResponseType = HttpResponse<ILoad>;
type EntityArrayResponseType = HttpResponse<ILoad[]>;

@Injectable({ providedIn: 'root' })
export class LoadService {
  public resourceUrl = SERVER_API_URL + 'api/loads';

  constructor(protected http: HttpClient) {}

  create(load: ILoad): Observable<EntityResponseType> {
    return this.http.post<ILoad>(this.resourceUrl, load, { observe: 'response' });
  }

  update(load: ILoad): Observable<EntityResponseType> {
    return this.http.put<ILoad>(this.resourceUrl, load, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
