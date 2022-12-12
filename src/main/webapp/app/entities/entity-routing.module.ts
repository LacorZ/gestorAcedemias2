import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'curso',
        data: { pageTitle: 'gestorAcedemias2App.curso.home.title' },
        loadChildren: () => import('./curso/curso.module').then(m => m.CursoModule),
      },
      {
        path: 'tutor',
        data: { pageTitle: 'gestorAcedemias2App.tutor.home.title' },
        loadChildren: () => import('./tutor/tutor.module').then(m => m.TutorModule),
      },
      {
        path: 'estudiante',
        data: { pageTitle: 'gestorAcedemias2App.estudiante.home.title' },
        loadChildren: () => import('./estudiante/estudiante.module').then(m => m.EstudianteModule),
      },
      {
        path: 'padre',
        data: { pageTitle: 'gestorAcedemias2App.padre.home.title' },
        loadChildren: () => import('./padre/padre.module').then(m => m.PadreModule),
      },
      {
        path: 'asistencia',
        data: { pageTitle: 'gestorAcedemias2App.asistencia.home.title' },
        loadChildren: () => import('./asistencia/asistencia.module').then(m => m.AsistenciaModule),
      },
      {
        path: 'factura',
        data: { pageTitle: 'gestorAcedemias2App.factura.home.title' },
        loadChildren: () => import('./factura/factura.module').then(m => m.FacturaModule),
      },
      {
        path: 'pago',
        data: { pageTitle: 'gestorAcedemias2App.pago.home.title' },
        loadChildren: () => import('./pago/pago.module').then(m => m.PagoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
