import { Component, inject, OnInit, ViewChild, ElementRef, AfterViewInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FinanceiroService, ResumoFinanceiro } from '../../services/financeiro.service';
import { AuthService } from '../../services/auth.service';
import { Chart, registerables } from 'chart.js';
 
Chart.register(...registerables);
 
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('barrasChart') barrasRef!: ElementRef;
  @ViewChild('pizzaChart') pizzaRef!: ElementRef;
 
  private financeiroService = inject(FinanceiroService);
  private authService = inject(AuthService);
  private router = inject(Router);
 
  resumo: ResumoFinanceiro | null = null;
  carregando = true;
  erro = '';
  nomeUsuario = '';
 
  private barrasChart: Chart | null = null;
  private pizzaChart: Chart | null = null;
  private dadosCarregados = false;
 
  ngOnInit(): void {
    const token = this.authService.getToken();
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.nomeUsuario = payload.sub?.split('@')[0] || 'Usuário';
      } catch { this.nomeUsuario = 'Usuário'; }
    }
    this.carregarResumo();
  }
 
  ngAfterViewInit(): void {
    if (this.dadosCarregados) this.criarGraficos();
  }
 
  carregarResumo(): void {
    this.financeiroService.getResumo().subscribe({
      next: (data) => {
        this.resumo = data;
        this.carregando = false;
        this.dadosCarregados = true;
        setTimeout(() => this.criarGraficos(), 100);
      },
      error: () => {
        this.erro = 'Erro ao carregar dados.';
        this.carregando = false;
      }
    });
  }
 
  criarGraficos(): void {
    if (!this.barrasRef || !this.pizzaRef) return;
 
    if (this.barrasChart) this.barrasChart.destroy();
    if (this.pizzaChart) this.pizzaChart.destroy();
 
    const lancamentos = this.resumo?.lancamentos ?? [];
 
    const receitas = lancamentos
      .filter(l => l.tipo === 'RECEITA')
      .reduce((acc, l) => acc + l.valor, 0);
 
    const despesas = lancamentos
      .filter(l => l.tipo === 'DESPESA')
      .reduce((acc, l) => acc + l.valor, 0);
 
    this.barrasChart = new Chart(this.barrasRef.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Receitas', 'Despesas'],
        datasets: [{
          label: 'Valor (R$)',
          data: [receitas, despesas],
          backgroundColor: ['rgba(0,200,240,0.7)', 'rgba(255,82,82,0.7)'],
          borderRadius: 6
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          x: { ticks: { color: '#4a7a9b' }, grid: { color: 'rgba(0,200,240,0.05)' } },
          y: { ticks: { color: '#4a7a9b' }, grid: { color: 'rgba(0,200,240,0.05)' } }
        }
      }
    });
 
    const categorias = lancamentos.reduce((acc: any, l) => {
      const nome = l.centroCustoNome || 'Outros';
      acc[nome] = (acc[nome] || 0) + l.valor;
      return acc;
    }, {});
 
    const cores = ['#00c8f0', '#00e676', '#ff5252', '#ffd740', '#b388ff', '#ff8a65'];
 
    this.pizzaChart = new Chart(this.pizzaRef.nativeElement, {
      type: 'doughnut',
      data: {
        labels: Object.keys(categorias).length ? Object.keys(categorias) : ['Sem dados'],
        datasets: [{
          data: Object.values(categorias).length ? Object.values(categorias) : [1],
          backgroundColor: Object.keys(categorias).length ? cores : ['#1a3a5c'],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: { color: '#4a7a9b', font: { size: 10 }, padding: 12 }
          }
        }
      }
    });
  }
 
  ngOnDestroy(): void {
    if (this.barrasChart) this.barrasChart.destroy();
    if (this.pizzaChart) this.pizzaChart.destroy();
  }
 
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
 
  formatarValor(valor: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
  }
}