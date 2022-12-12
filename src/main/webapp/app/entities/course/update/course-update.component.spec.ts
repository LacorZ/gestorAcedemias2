import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CourseFormService } from './course-form.service';
import { CourseService } from '../service/course.service';
import { ICourse } from '../course.model';
import { IAttendance } from 'app/entities/attendance/attendance.model';
import { AttendanceService } from 'app/entities/attendance/service/attendance.service';

import { CourseUpdateComponent } from './course-update.component';

describe('Course Management Update Component', () => {
  let comp: CourseUpdateComponent;
  let fixture: ComponentFixture<CourseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseFormService: CourseFormService;
  let courseService: CourseService;
  let attendanceService: AttendanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CourseUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CourseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseFormService = TestBed.inject(CourseFormService);
    courseService = TestBed.inject(CourseService);
    attendanceService = TestBed.inject(AttendanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Attendance query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const attendance: IAttendance = { id: 8924 };
      course.attendance = attendance;

      const attendanceCollection: IAttendance[] = [{ id: 15832 }];
      jest.spyOn(attendanceService, 'query').mockReturnValue(of(new HttpResponse({ body: attendanceCollection })));
      const additionalAttendances = [attendance];
      const expectedCollection: IAttendance[] = [...additionalAttendances, ...attendanceCollection];
      jest.spyOn(attendanceService, 'addAttendanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(attendanceService.query).toHaveBeenCalled();
      expect(attendanceService.addAttendanceToCollectionIfMissing).toHaveBeenCalledWith(
        attendanceCollection,
        ...additionalAttendances.map(expect.objectContaining)
      );
      expect(comp.attendancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const course: ICourse = { id: 456 };
      const attendance: IAttendance = { id: 81996 };
      course.attendance = attendance;

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(comp.attendancesSharedCollection).toContain(attendance);
      expect(comp.course).toEqual(course);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseFormService, 'getCourse').mockReturnValue(course);
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseFormService.getCourse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseService.update).toHaveBeenCalledWith(expect.objectContaining(course));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseFormService, 'getCourse').mockReturnValue({ id: null });
      jest.spyOn(courseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseFormService.getCourse).toHaveBeenCalled();
      expect(courseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAttendance', () => {
      it('Should forward to attendanceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(attendanceService, 'compareAttendance');
        comp.compareAttendance(entity, entity2);
        expect(attendanceService.compareAttendance).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
