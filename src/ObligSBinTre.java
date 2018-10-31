import java.util.*;
public class ObligSBinTre<T> implements Beholder<T>
{
    private static final class Node<T> // en indre nodeklasse
    {
        private T verdi; // nodens verdi
        private Node<T> venstre, høyre; // venstre og høyre barn
        private Node<T> forelder; // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
        {
            this.verdi = verdi;
            venstre = v; høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder) // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString(){
            return "" + verdi;
        }
    } // class Node


    private Node<T> rot; // peker til rotnoden
    private int antall; // antall noder
    private int endringer; // antall endringer
    private final Comparator<? super T> comp; // komparator

    public ObligSBinTre(Comparator<? super T> c) // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public final boolean leggInn(T verdi)    // skal ligge i class SBinTre
    {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)                        // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        p = new Node<T>(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi)
    {
        if (verdi == null) return false;
        Node<T> p = rot;
        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi)
    {
        if (verdi == null) return false;
        Node<T> p = rot;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else break;
        }
        if(p == null) return false;

        Node<T> q = p.forelder;
        if (p.venstre == null || p.høyre == null)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;
            if (p == rot) rot = b;
            else if (p == q.venstre) q.venstre = b;
            else q.høyre = b;

            if(b != null) b.forelder = q;
        }

        else {
            Node<T> s = p, r = p.høyre;
            while (r.venstre != null)
            {
                s = r;
                r = r.venstre;
            }

            p.verdi = r.verdi;

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;

            if(r.høyre != null)
                r.høyre.forelder = s;
        }

        antall--;
        return true;
    }

    public int fjernAlle(T verdi)
    {
        if(verdi == null || rot == null) return 0;

        int fjernet = antall(verdi);
        for (int i = 0; i < fjernet; i++) {
            fjern(verdi);
        }
        return fjernet;
    }

    @Override
    public int antall()
    {
        return antall;
    }

    public int antall(T verdi)
    {
        if(verdi == null) return 0;
        int antall_forekomster = 0;
        Node<T> p = rot;
        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else {
                antall_forekomster++;
                p = p.høyre;
            }
        }
        return antall_forekomster;
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public void nullstill()
    {
        if(tom()) return;
        nullstill(rot);
        rot = null;
        antall = 0;
    }

    private void nullstill(Node<T> p) {
        if(p.venstre != null) {
            nullstill(p.venstre);
        }
        if(p.høyre != null) {
            nullstill(p.høyre);
        }

        if(p.venstre != null) {
            p.venstre.forelder = null;
        }
        if(p.høyre != null) {
            p.høyre.forelder = null;
        }

        p.venstre = null;
        p.høyre = null;
        p.verdi = null;

    }

    private static <T> Node<T> nesteInorden(Node<T> p)
    {
        if(p == null) {
            throw new NullPointerException("Noden p er null");
        }

        if(p.høyre != null) {
            p = p.høyre;
            while (p.venstre != null) {
                p = p.venstre;
            }
            return p;
        }
        else if (p.forelder != null){
            Node<T> q = p;
            p = p.forelder;
            while (p != null && p.høyre == q) {
                q = p;
                p = p.forelder;
            }
            return p;
        }
        else {
            return null;
        }
    }

    @Override
    public String toString()
    {
        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = rot;
        if(rot != null) {
            while (p.venstre != null) p = p.venstre;

            while (p != null) {
                s.add(p.verdi.toString());
                p = nesteInorden(p);
            }
        }

        return s.toString();
    }

    public String omvendtString()
    {
        ArrayDeque<Node<T>> stakk = new ArrayDeque<>();
        Node<T> p = rot;
        StringJoiner s = new StringJoiner(", ", "[", "]");

        if(p == null) return "[]";

        while (p != null || !stakk.isEmpty()) {
            while (p != null) {
                stakk.add(p);
                p = p.høyre;
            }
            p = stakk.pollLast();
            s.add(p.verdi.toString());
            p = p.venstre;
        }
        return s.toString();
    }

    public String høyreGren()
    {
        if(tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");
        Node<T> p = rot;
        while (p != null) {
            s.add(p.verdi.toString());
            p = p.høyre;
        }
        return s.toString();
    }

    public String lengstGren() {

        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    public String[] grener()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier()
    {
        if(rot == null) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");
        bladnodeverdier(rot, s);
        return s.toString();
    }

    private void bladnodeverdier(Node<T> p, StringJoiner s) {
        if(p.venstre != null) {
            bladnodeverdier(p.venstre, s);
        }
        if(p.høyre != null) {
            bladnodeverdier(p.høyre, s);
        }
        if(p.høyre == null && p.venstre == null) {
            s.add(p.verdi.toString());
        }
    }

    public String postString()
    {
        if(rot == null) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");
        Stack<Node<T>> deque = new Stack<>();
        Node<T> p = rot;
        Node<T> q = null;
        deque.add(p);

        while (!deque.isEmpty()) {
            p = deque.peek();

            if(q == null || p == q.venstre || p == q.høyre) {
                if(p.venstre != null) {
                    deque.push(p.venstre);
                }
                else if(p.høyre != null) {
                    deque.push(p.høyre);
                }
                else {
                    deque.pop();
                    s.add(p.verdi.toString());
                }
            }

            else if(p.venstre == q) {
                if(p.høyre != null) {
                    deque.add(p.høyre);
                }
                else {
                    deque.pop();
                    s.add(p.toString());
                }
            }
            else if(p.høyre == q) {
                deque.pop();
                s.add(p.verdi.toString());
            }

            q = p;
        }

        return s.toString();
    }

    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() // konstruktør
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext()
        {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }
    } // BladnodeIterator

} // ObligSBinTre