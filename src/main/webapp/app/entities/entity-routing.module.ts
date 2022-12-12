import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'course',
        data: { pageTitle: 'gestorAcedemias2App.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'tutor',
        data: { pageTitle: 'gestorAcedemias2App.tutor.home.title' },
        loadChildren: () => import('./tutor/tutor.module').then(m => m.TutorModule),
      },
      {
        path: 'student',
        data: { pageTitle: 'gestorAcedemias2App.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'attendance',
        data: { pageTitle: 'gestorAcedemias2App.attendance.home.title' },
        loadChildren: () => import('./attendance/attendance.module').then(m => m.AttendanceModule),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'gestorAcedemias2App.invoice.home.title' },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'gestorAcedemias2App.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'payment-method',
        data: { pageTitle: 'gestorAcedemias2App.paymentMethod.home.title' },
        loadChildren: () => import('./payment-method/payment-method.module').then(m => m.PaymentMethodModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
