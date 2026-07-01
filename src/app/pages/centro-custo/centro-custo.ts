import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CentroCustoService, CentroCustoDTO } from '../../services/centro-custo.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-centro-custo',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './centro-custo.html',
  styleUrl: './centro-custo.scss',
})
export class CentroCusto implements OnInit {
  private centroCustoService = inject(CentroCustoService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  centros: CentroCustoDTO[] = [];
  carregando = true;
  salvando = false;
  mostrarFormulario = false;
  editandoId: number | null = null;
  confirmarExclusaoId: number | null = null;

  colunas = ['nome', 'acoes'];

  form: FormGroup = this.fb.group({
    nome: ['', [Validators.required, Validators.minLength(2)]]
  });

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.centroCustoService.listar().subscribe({
      next: (data) => {
        this.centros = data.sort((a, b) => a.nome.localeCompare(b.nome, 'pt-BR'));
        this.carregando = false;
      },
      error: () => {
        this.snackBar.open('Erro ao carregar centros de custo.', 'Fechar', {duration: 3000});
        this.carregando = false;
      }
    });
  }

  abrirNovo(): void {
    this.editandoId = null;
    this.form.reset();
    this.mostrarFormulario = true;
    this.confirmarExclusaoId = null;
  }

  editar(centro: CentroCustoDTO): void {
    this.editandoId = centro.id ?? null;
    this.form.patchValue({nome: centro.nome});
    this.mostrarFormulario = true;
    this.confirmarExclusaoId = null;
  }

  fecharFormulario(): void {
    this.mostrarFormulario = false;
    this.editandoId = null;
    this.form.reset();
  }

  salvar(): void {
    if (this.form.invalid) return;
    this.salvando = true;
    const dados: CentroCustoDTO = { nome: this.form.value.nome };

    const operacao = this.editandoId
      ? this.centroCustoService.atualizar(this.editandoId, dados)
      : this.centroCustoService.criar(dados);

    operacao.subscribe({
      next: () => {
        this.snackBar.open(
          this.editandoId ? 'Atualizado com sucesso!' : 'Criado com sucesso!', 'Fechar', { duration: 3000 }
        );
        this.fecharFormulario();
        this.carregar();
        this.salvando = false;
      },
      error: () => {
        this.snackBar.open('Erro ao salvar. Tente novamete.', 'Fechar', { duration: 3000});
        this.salvando = false;
      }
    });  
  }

  pedirConfirmacao(id: number): void {
    this.confirmarExclusaoId = id;
    this.mostrarFormulario = false;
  }

  cancelarExclusao(): void {
    this.confirmarExclusaoId = null;
  }

  excluir(id: number): void {
    this.centroCustoService.excluir(id).subscribe({
      next: () => {
        this.snackBar.open('Excluido com sucesso!', 'Fechar', { duration: 3000});
        this.confirmarExclusaoId = null;
        this.carregar();
      },
      error: () => {
        this.snackBar.open('Erro ao excluir.', 'Fechar', { duration: 3000});
        this.confirmarExclusaoId = null;
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
