import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILoadShear } from 'app/shared/model/load-shear.model';

type EntityResponseType = HttpResponse<ILoadShear>;
type EntityArrayResponseType = HttpResponse<ILoadShear[]>;

@Injectable({ providedIn: 'root' })
export class LoadShearService {
  public resourceUrl = SERVER_API_URL + 'api/load-shears';

  constructor(protected http: HttpClient) {}

  create(loadShear: ILoadShear): Observable<EntityResponseType> {
    return this.http.post<ILoadShear>(this.resourceUrl, loadShear, { observe: 'response' });
  }

  update(loadShear: ILoadShear): Observable<EntityResponseType> {
    return this.http.put<ILoadShear>(this.resourceUrl, loadShear, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoadShear>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoadShear[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
