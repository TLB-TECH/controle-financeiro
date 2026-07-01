import { inject, Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { environment } from "../../environments/environment.prod";
import { Observable } from "rxjs";

export interface CentroCustoDTO {
    id?: number;
    nome: string;
}

@Injectable({ providedIn: 'root'})
export class CentroCustoService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/centros-custo`;

    listar(): Observable<CentroCustoDTO[]> {
        return this.http.get<CentroCustoDTO[]>(this.apiUrl);
    }

    criar(centro: CentroCustoDTO): Observable<CentroCustoDTO> {
        return this.http.post<CentroCustoDTO>(this.apiUrl, centro);
    }

    atualizar(id: number, centro: CentroCustoDTO): Observable<CentroCustoDTO> {
        return this.http.put<CentroCustoDTO>(`${this.apiUrl}/${id}`, centro);
    }

    excluir(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}