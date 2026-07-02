import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../services/auth.service';
import { LogoService } from '../../services/logo.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, MatIconModule, MatButtonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  @Input() activePage: 'dashboard' | 'lancamentos' | 'centro-custo' = 'dashboard';
  private authService = inject(AuthService);
  private router = inject(Router);
  logoService = inject(LogoService);
  ano = new Date().getFullYear();

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}