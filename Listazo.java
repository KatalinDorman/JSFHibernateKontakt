/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import org.hibernate.Query;
import org.hibernate.Session;
import pojos.Contact;
import pojos.Phone;

/**
 *
 * @author Katalin
 */
@ManagedBean
@SessionScoped
public class Listazo {

    private List<Contact> kontaktok;
    private List<Phone> telefonszamok;
    private Phone telefonszam;
    private String type;
    private int kivalasztottKontaktId;
    private Contact kivalasztottKontakt;
    private Map<Integer, Contact> kontaktMap;
    private String nevKeres;

    public Listazo() {
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();

        kontaktok = session.createQuery("FROM Contact").list();
        kontaktMap = new HashMap<>();
        for (Contact k : kontaktok) {
            kontaktMap.put(k.getId(), k);
        }

        session.close();
    }

    public void kontaktValaszt() {

        kivalasztottKontakt = kontaktMap.get(kivalasztottKontaktId);
        telefonszamok = new ArrayList<>(kivalasztottKontakt.getPhones());

    }

    public void keres() {
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();

        Query q = session.createQuery("From Contact Where name LIKE :pname ORDER BY name");
        q.setString("pname", nevKeres + "%");
        kontaktok = q.list();

        session.close();
    }

    public String szerkeszt(Contact kontakt) {
        kivalasztottKontakt = kontakt;
        return "kontakt_szerkeszt";
    }

    public String szamSzerkeszt(Phone szam) {
        telefonszam = szam;
        return "szam_szerkeszt";
    }

    public String szamMent() {
        boolean uj = kivalasztottKontakt.getId() == null;
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(telefonszam);
        session.getTransaction().commit();
        session.close();

        if (uj) {
            telefonszamok.add(telefonszam);
            kivalasztottKontakt.getPhones().add(telefonszam);
            return "index";
        }

        return "index";
    }

    public String kontaktMent() {
        boolean uj = kivalasztottKontakt.getId() == null;
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(kivalasztottKontakt);
        session.getTransaction().commit();
        session.close();

        if (uj) {
            kontaktok.add(kivalasztottKontakt);
            kontaktMap.put(kivalasztottKontakt.getId(), kivalasztottKontakt);
        }

        return "index";
    }

    public String ujKontakt() {

        kivalasztottKontakt = new Contact();

        return "kontakt_szerkeszt";
    }

    public String ujSzam(Contact kontakt) {

        telefonszam = new Phone(kontakt);
        telefonszamok.add(telefonszam);

        return "szam_szerkeszt";
    }

    public void szamTorol(Phone telefonszam) {
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(telefonszam);
        session.getTransaction().commit();
        session.close();
        telefonszamok.remove(telefonszamok);
        kivalasztottKontakt.getPhones().remove(telefonszam);
    }

    public List<Contact> getKontaktok() {
        return kontaktok;
    }

    public void setKontaktok(List<Contact> kontaktok) {
        this.kontaktok = kontaktok;
    }

    public List<Phone> getTelefonszamok() {
        return telefonszamok;
    }

    public void setTelefonszamok(List<Phone> telefonszamok) {
        this.telefonszamok = telefonszamok;
    }

    public int getKivalasztottKontaktId() {
        return kivalasztottKontaktId;
    }

    public void setKivalasztottKontaktId(int kivalasztottKontaktId) {
        this.kivalasztottKontaktId = kivalasztottKontaktId;
    }

    public Contact getKivalasztottKontakt() {
        return kivalasztottKontakt;
    }

    public void setKivalasztottKontakt(Contact kivalasztottKontakt) {
        this.kivalasztottKontakt = kivalasztottKontakt;
    }

    public Map<Integer, Contact> getKontaktMap() {
        return kontaktMap;
    }

    public void setKontaktMap(Map<Integer, Contact> kontaktMap) {
        this.kontaktMap = kontaktMap;
    }

    public String getNevKeres() {
        return nevKeres;
    }

    public void setNevKeres(String nevKeres) {
        this.nevKeres = nevKeres;
    }

    public Phone getTelefonszam() {
        return telefonszam;
    }

    public void setTelefonszam(Phone telefonszam) {
        this.telefonszam = telefonszam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
