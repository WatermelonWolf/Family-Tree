import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FamilyTree
{
    public static void main(String[] args)
    {
        Scanner scan=new Scanner(System.in);
        SupportTree tree=new SupportTree();
        while (scan.hasNextLine())
        {
            String line=scan.nextLine();
            char type=line.charAt(0);
            String[] names=line.split(" ");
            String p;
            ArrayList<String> l;
            boolean b=false;
            int n=-1;
            switch (type)
            {
                case 'E':
                    if (names.length==4)
                        tree.addChild(names[1], names[2], names[3]);
                    else if (names.length==3)
                        tree.marryPeople(names[1], names[2]);
                    else
                        System.out.println("ERROR");
                    break;
                case 'W':
                    System.out.println(line);
                    l=new ArrayList<String>();
                    if (names.length==4)
                    {
                        n=Integer.parseInt(names[2]);
                        p=names[3];
                    }
                    else
                        p=names[2];
                    if (names[1].equals("child"))
                        l=tree.getChildren(p);
                    else if (names[1].equals("sibling"))
                        l=tree.getSiblings(p);
                    else if (names[1].equals("ancestor"))
                        l=tree.getAncestors(p);
                    else if (names[1].equals("cousin"))
                    {
                        if (n==-1)
                            l=tree.getAllCousins(p);
                        else
                            l=tree.getNCousins(p, n);
                    }
                    else if (names[1].equals("unrelated"))
                        l=tree.getUnrelated(p);
                    else
                        System.out.println("ERROR");
                    Collections.sort(l);
                    for (String s : l)
                    {
                        System.out.println(s);
                    }
                    System.out.println();
                    n=-1;
                    break;
                case 'X':
                    System.out.println(line);
                    if (names.length==5)
                    {
                        n=Integer.parseInt(names[3]);
                        p=names[4];
                    }
                    else
                        p=names[3];
                    if (names[2].equals("child"))
                        b=tree.child(p, names[1]);
                    else if (names[2].equals("sibling"))
                        b=tree.siblings(names[1], p);
                    else if (names[2].equals("ancestor"))
                        b=tree.ancestor(names[1], p);
                    else if (names[2].equals("cousin"))
                    {
                        if (n==-1)
                            b=tree.cousin(names[1], p);
                        else
                            b=tree.nCousin(names[1], p, n);
                    }
                    else if (names[2].equals("unrelated"))
                        b=tree.unrelated(names[1], p);
                    else
                        System.out.println("ERROR");
                    if (b)
                        System.out.println("Yes");
                    else
                        System.out.println("No");
                    System.out.println();
                    n=-1;
                    break;
                default:
                    System.out.println("ERROR");
                    break;
            }
        }
    }
}