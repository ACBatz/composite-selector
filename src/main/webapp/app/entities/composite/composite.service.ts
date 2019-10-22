import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IComposite } from 'app/shared/model/composite.model';

type EntityResponseType = HttpResponse<IComposite>;
type EntityArrayResponseType = HttpResponse<IComposite[]>;

@Injectable({ providedIn: 'root' })
export class CompositeService {
  public resourceUrl = SERVER_API_URL + 'api/composites';

  constructor(protected http: HttpClient) {}

  create(composite: IComposite): Observable<EntityResponseType> {
    return this.http.post<IComposite>(this.resourceUrl, composite, { observe: 'response' });
  }

  update(composite: IComposite): Observable<EntityResponseType> {
    return this.http.put<IComposite>(this.resourceUrl, composite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IComposite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComposite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
