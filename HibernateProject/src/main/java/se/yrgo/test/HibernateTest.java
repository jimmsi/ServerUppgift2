package se.yrgo.test;

import jakarta.persistence.*;

import se.yrgo.domain.Student;
import se.yrgo.domain.Subject;
import se.yrgo.domain.Tutor;

import java.util.List;

public class HibernateTest
{
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("databaseConfig");

    public static void main(String[] args){
        setUpData();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /*

        Query q = em.createQuery("from Student");
        List<Student> students = q.getResultList();
        for(Student student:students) {
            System.out.println(student);
        }

        TypedQuery<Student> q1 = em.createQuery("from Student as student where student.name = 'Jimi Hendriks' ", Student.class);
        List<Student> myStudents = q1.getResultList();
        for(Student student:myStudents) {
            System.out.println(student);
        }

        TypedQuery<Student> q2 = em.createQuery("from Student as student where student.name like 'Jim%'", Student.class);
        List<Student> myOtherStudents = q2.getResultList();
        for(Student student:myOtherStudents) {
            System.out.println(student);
        }

        q= em.createQuery("FROM Student as student WHERE student.enrollmentID = '1-HEN-2019' ", Student.class);
        Student theStudent = (Student) q.getSingleResult();
        System.out.println(theStudent);

        String requiredName = "jimi hendriks";
        q=em.createQuery("FROM Student as student WHERE lower(student.name) =:name", Student.class);
        q.setParameter("name", requiredName);
        List<Student>QueryResult =q.getResultList();
        for(Student st1:QueryResult) {
            System.out.println(st1);
        }
        */

        Query q2 =em.createQuery("select tutor.teachingGroup from Tutor as tutor where tutor.name= 'Johan Smith'");
        List<Student> studentsForJohan = q2.getResultList();
        for (Student s : studentsForJohan) {
            System.out.println(s);
        }

        Subject programming = em.find(Subject.class, 3);
        TypedQuery<Tutor> query= em.createQuery("from Tutor tutor where :subject member of tutor.subjectsToTeach",Tutor.class);
        query.setParameter("subject", programming);
        List<Tutor>tutorsForProgramming = query.getResultList();
        for(Tutor tutor : tutorsForProgramming) {
            System.out.println(tutor);
        }

        tx.commit();
        em.close();
    }

    public static void setUpData(){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Subject mathematics = new Subject("Mathematics", 2);
        Subject science = new Subject("Science", 2);
        Subject programming = new Subject("Programming", 3);
        em.persist(mathematics);
        em.persist(science);
        em.persist(programming);

        Tutor t1 = new Tutor("ABC123", "Johan Smith", 40000);
        t1.addSubjectsToTeach(mathematics);
        t1.addSubjectsToTeach(science);

        Tutor t2 = new Tutor("DEF456", "Sara Svensson", 20000);
        t2.addSubjectsToTeach(mathematics);
        t2.addSubjectsToTeach(science);

        // This tutor is the only tutor who can teach History
        Tutor t3 = new Tutor("GHI678", "Karin Lindberg", 0);
        t3.addSubjectsToTeach(programming);

        em.persist(t1);
        em.persist(t2);
        em.persist(t3);

        t1.createStudentAndAddtoTeachingGroup("Jimi Hendriks", "1-HEN-2019", "Street 1", "city 1", "1212");
        t1.createStudentAndAddtoTeachingGroup("Bruce Lee", "2-LEE-2019", "Street 2", "city 2", "2323");
        t3.createStudentAndAddtoTeachingGroup("Roger Waters", "3-WAT-2018", "Street 3", "city 3", "34343");

        tx.commit();
        em.close();
    }

}

