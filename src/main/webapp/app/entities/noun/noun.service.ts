import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INoun } from 'app/shared/model/noun.model';

type EntityResponseType = HttpResponse<INoun>;
type EntityArrayResponseType = HttpResponse<INoun[]>;

@Injectable({ providedIn: 'root' })
export class NounService {
  public resourceUrl = SERVER_API_URL + 'api/nouns';

  constructor(protected http: HttpClient) {}

  create(noun: INoun): Observable<EntityResponseType> {
    return this.http.post<INoun>(this.resourceUrl, noun, { observe: 'response' });
  }

  update(noun: INoun): Observable<EntityResponseType> {
    return this.http.put<INoun>(this.resourceUrl, noun, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INoun>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INoun[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
