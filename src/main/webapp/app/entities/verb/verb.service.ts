import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVerb } from 'app/shared/model/verb.model';

type EntityResponseType = HttpResponse<IVerb>;
type EntityArrayResponseType = HttpResponse<IVerb[]>;

@Injectable({ providedIn: 'root' })
export class VerbService {
  public resourceUrl = SERVER_API_URL + 'api/verbs';

  constructor(protected http: HttpClient) {}

  create(verb: IVerb): Observable<EntityResponseType> {
    return this.http.post<IVerb>(this.resourceUrl, verb, { observe: 'response' });
  }

  update(verb: IVerb): Observable<EntityResponseType> {
    return this.http.put<IVerb>(this.resourceUrl, verb, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVerb>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVerb[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
