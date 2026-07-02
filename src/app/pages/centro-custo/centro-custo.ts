import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CentroCustoService, CentroCustoDTO } from '../../services/centro-custo.service';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';

@Component({
  selector: 'app-centro-custo',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    SidebarComponent
  ],
  templateUrl: './centro-custo.html',
  styleUrl: './centro-custo.scss'
})
export class CentroCusto implements OnInit {
  private centroCustoService = inject(CentroCustoService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  centros: CentroCustoDTO[] = [];
  carregando = true;
  salvando = false;
  mostrarFormulario = false;
  editandoId: number | null = null;
  confirmarInativacaoId: number | null = null;

  // Filtro: true = ativos, false = inativos, null = todos
  filtroAtivo: boolean | null = true;

  colunas = ['nome', 'status', 'acoes'];

  form: FormGroup = this.fb.group({
    nome: ['', [Validators.required, Validators.minLength(2)]]
  });

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.centroCustoService.listar(this.filtroAtivo).subscribe({
      next: (data) => {
        this.centros = data;
        this.carregando = false;
      },
      error: () => {
        this.snackBar.open('Erro ao carregar centros de custo.', 'Fechar', { duration: 3000 });
        this.carregando = false;
      }
    });
  }

  setFiltro(valor: boolean | null): void {
    this.filtroAtivo = valor;
    this.fecharFormulario();
    this.confirmarInativacaoId = null;
    this.carregar();
  }

  abrirNovo(): void {
    this.editandoId = null;
    this.form.reset();
    this.mostrarFormulario = true;
    this.confirmarInativacaoId = null;
  }

  editar(centro: CentroCustoDTO): void {
    this.editandoId = centro.id ?? null;
    this.form.patchValue({ nome: centro.nome });
    this.mostrarFormulario = true;
    this.confirmarInativacaoId = null;
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
          this.editandoId ? 'Atualizado com sucesso!' : 'Criado com sucesso!',
          'Fechar', { duration: 3000 }
        );
        this.fecharFormulario();
        this.carregar();
        this.salvando = false;
      },
      error: () => {
        this.snackBar.open('Erro ao salvar. Tente novamente.', 'Fechar', { duration: 3000 });
        this.salvando = false;
      }
    });
  }

  pedirConfirmacaoInativacao(id: number): void {
    this.confirmarInativacaoId = id;
    this.mostrarFormulario = false;
  }

  cancelarInativacao(): void {
    this.confirmarInativacaoId = null;
  }

  inativar(id: number): void {
    this.centroCustoService.inativar(id).subscribe({
      next: () => {
        this.snackBar.open('Inativado com sucesso!', 'Fechar', { duration: 3000 });
        this.confirmarInativacaoId = null;
        this.carregar();
      },
      error: () => {
        this.snackBar.open('Erro ao inativar.', 'Fechar', { duration: 3000 });
        this.confirmarInativacaoId = null;
      }
    });
  }

  reativar(id: number): void {
    this.centroCustoService.reativar(id).subscribe({
      next: () => {
        this.snackBar.open('Reativado com sucesso!', 'Fechar', { duration: 3000 });
        this.carregar();
      },
      error: () => {
        this.snackBar.open('Erro ao reativar.', 'Fechar', { duration: 3000 });
      }
    });
  }
}