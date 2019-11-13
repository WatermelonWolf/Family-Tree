import java.util.HashMap;
import java.util.ArrayList;

public class SupportTree
{
    private class PeopleObject
    {
        public String name;
        public String parentA;
        public String parentB;
        public ArrayList<String> marriages;
        public PeopleObject(String personName)
        {
            name=personName;
            parentA="";
            parentB="";
            marriages=new ArrayList<>();
        }
        public PeopleObject(String p1, String p2, String personName)
        {
            parentA=p1;
            parentB=p2;
            name=personName;
            marriages=new ArrayList<String>();
        }
    }
    private ArrayList<PeopleObject> family;
    public SupportTree()
    {
        family=new ArrayList<PeopleObject>();
    }
    private PeopleObject getPeopleObject(String p)
    {
        PeopleObject person=null;
        for (PeopleObject per : family)
        {
            if (per.name.equals(p))
            {
                person=per;
                break;
            }
        }
        return person;
    }
    private int getPeopleObjectIndex(String p)
    {
        int index=-1;
        for (int i=0; i<family.size(); i++)
        {
            if (family.get(i).name.equals(p))
            {
                index=i;
                break;
            }
        }
        return index;
    }
    public boolean hasPerson(String name)
    {
        return getPeopleObjectIndex(name)!=-1;
    }
    private void updatePeopleObject(String p, PeopleObject per)
    {
        int index=getPeopleObjectIndex(p);
        if (index!=-1)
        {
            family.remove(index);
            family.add(per);
        }
    }
    public void marryPeople(String p1, String p2)
    {
        createMarriage(p1, p2);
        createMarriage(p2, p1);
    }
    private void createMarriage(String p1, String p2)
    {
        if (hasPerson(p1))
        {
            PeopleObject per=getPeopleObject(p1);
            if (!per.marriages.contains(p1))
            {
                per.marriages.add(p1);
                updatePeopleObject(p1, per);
            }
        }
        else
        {
            PeopleObject per=new PeopleObject(p1);
            per.marriages.add(p2);
            family.add(per);
        }
    }
    public void addChild(String p1, String p2, String childName)
    {
        PeopleObject per=new PeopleObject(p1, p2, childName);
        family.add(per);
        marryPeople(p1, p2);
    }
    public boolean child(String p, String c)
    {
        PeopleObject parent=getPeopleObject(p), child=getPeopleObject(c);
        if (child.parentA.equals(parent.name) || child.parentB.equals(parent.name))
            return true;
        return false;
    }
    public ArrayList<String> getChildren(String p)
    {
        ArrayList<String> children=new ArrayList<String>();
        for (PeopleObject per : family)
            if (child(p, per.name))
                children.add(per.name);
        return children;
    }
    public boolean siblings(String p1, String p2)
    {
        PeopleObject per1=getPeopleObject(p1), per2=getPeopleObject(p2);
        if (per1.parentA.equals("") || per2.parentA.equals(""))
            return false;
        else if (p1.equals(p2))
            return false;
        else if (per1.parentA.equals(per2.parentA) || per1.parentA.equals(per2.parentB)
        || per1.parentB.equals(per2.parentA) || per1.parentB.equals(per2.parentB))
            return true;
        return false;
    }
    public ArrayList<String> getSiblings(String p)
    {
        ArrayList<String> siblings=new ArrayList<>();
        for (PeopleObject per : family)
            if (siblings(per.name, p))
                siblings.add(per.name);
        return siblings;
    }
    private HashMap<String, Integer> ancestorTable(String name, int n)
    {
        HashMap<String, Integer> table=new HashMap<>();
        PeopleObject p=getPeopleObject(name);
        if(!p.parentA.equals(""))
        {
            PeopleObject pA=getPeopleObject(p.parentA);
            PeopleObject pB=getPeopleObject(p.parentB);
            if (!table.containsKey(pA.name))
                table.put(pA.name, n);
            if (!table.containsKey(pB.name))
                table.put(pB.name, n);
            HashMap<String, Integer> ancA=ancestorTable(pA.name, n + 1);
            HashMap<String, Integer> ancB=ancestorTable(pB.name, n + 1);
            table.putAll(ancA);
            table.putAll(ancB);
        }
        return table;
    }
    public ArrayList<String> getAncestors(String name)
    {
        HashMap<String, Integer> table=ancestorTable(name, 0);
        ArrayList<String> ancestors=new ArrayList<String>();
        ancestors.addAll(table.keySet());
        return ancestors;
    }
    public boolean ancestor(String a, String p)
    {
        ArrayList<String> anc=getAncestors(p);
        if (anc.contains(a))
            return true;
        return false;
    }
    public boolean related(String p1, String p2)
    {
        ArrayList<String> anc1=getAncestors(p1);
        ArrayList<String> anc2=getAncestors(p2);
        ArrayList<String> shared=new ArrayList<String>();
        if ((anc1.contains(p2) || anc2.contains(p1)) || p1.equals(p2))
            return true;
        for (String a : anc1)
            for (String b : anc2)
                if (a.equals(b))
                    shared.add(a);
        if (shared.isEmpty())
            return false;
        else
            return true;
    }
    public ArrayList<String> getRelated(String p)
    {
        ArrayList<String> related=new ArrayList<String>();
        for (PeopleObject per : family)
            if (related(per.name, p))
                related.add(per.name);
        return related;
    }
    public boolean unrelated(String p1, String p2)
    {
        if (!related(p1, p2))
            return true;
        return false;
    }
    public ArrayList<String> getUnrelated(String p)
    {
        ArrayList<String> unrelated=new ArrayList<String>();
        for (PeopleObject per : family)
            if (unrelated(per.name, p))
                unrelated.add(per.name);
        return unrelated;
    }
    public ArrayList<String> getAllCousins(String p)
    {
        ArrayList<String> cousins=new ArrayList<String>();
        ArrayList<String> related=getRelated(p);
        related.remove(p);
        related.removeAll(getAncestors(p));
        related.removeAll(getChildren(p));
        for (String r : related)
            if (!ancestor(p, r))
                cousins.add(r);
        return cousins;
    }
    public boolean cousin(String c, String p)
    {
        ArrayList<String> cousins=getAllCousins(p);
        if (cousins.contains(c))
            return true;
        return false;
    }
    public boolean nCousin(String c, String p, int n)
    {
        if (!cousin(c, p))
            return false;
        HashMap<String, Integer> ancC=ancestorTable(c, 0);
        HashMap<String, Integer> ancP=ancestorTable(p, 0);
        int min = Integer.MAX_VALUE;
        for (String aC : ancC.keySet())
        {
            for (String aP : ancP.keySet())
            {
                if (aC.equals(aP))
                {
                    if (ancC.get(aC)<ancP.get(aP) && ancC.get(aC)<min)
                        min=ancC.get(aC);
                    else if (ancP.get(aP)<min)
                        min=ancP.get(aP);
                }
            }
        }
        if (min==n)
            return true;
        return false;
    }
    public ArrayList<String> getNCousins(String p, int n)
    {
        ArrayList<String> nCousins=new ArrayList<String>();
        for (PeopleObject per : family)
            if (nCousin(per.name, p, n))
                nCousins.add(per.name);
        return nCousins;

    }
}