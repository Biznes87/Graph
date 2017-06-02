import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dex on 5/21/17.
 */
public class FindMachinery {
    public static void initProgram(String csvFile, int stPoint){

        // Задаем графы

        Graph theGraph = new Graph();

//инициализация

        Vertex v0 = new Vertex("Field1",0,"A");
        Vertex v1 = new Vertex("Field2",1,"B");
        Vertex v2 = new Vertex("Field3",2,"B");
        Vertex v3 = new Vertex("Field4",3,"B");
        Vertex v4 = new Vertex("Field5",4,"C");



        theGraph.addEdge(0,1,50);
        theGraph.addEdge(0,3,80);   //0,4,70
        theGraph.addEdge(1,2,60);   //1,2,60/
        theGraph.addEdge(1,3,90);
        theGraph.addEdge(2,4,40);
        theGraph.addEdge(3,2,20);
        theGraph.addEdge(3,4,70);
        theGraph.addEdge(1,4,50);

        theGraph.addEdge(1,0,50);
        theGraph.addEdge(3,0,80);   //0,4,70
        theGraph.addEdge(2,1,60);   //1,2,60/
        theGraph.addEdge(3,1,90);
        theGraph.addEdge(4,2,40);
        theGraph.addEdge(2,3,20);
        theGraph.addEdge(4,3,70);
        theGraph.addEdge(4,1,50);

//изменение зависящие от дельты со значением по умолчанию

        Vertex.shiftIndex(stPoint);
        Vertex.sortVertexBox();
        for (int i = 0; i <Vertex.vertexBox.size() ; i++) {
            theGraph.addVertex(Vertex.vertexBox.get(i));
        }

      //  theGraph.printAdjMatShift();
        theGraph.changeAddMat(stPoint);
       // System.out.println();
        //theGraph.printAdjMat();

    /*
        Vertex v0 = new Vertex('A',0);
        Vertex v1 = new Vertex('B',1);
        Vertex v2 = new Vertex('C',2);
        Vertex v3 = new Vertex('D',3);
        Vertex v4 = new Vertex('E',4);
      //  Vertex v5 = new Vertex('F',5);
   Vertex.shiftIndex(stPoint);
        Vertex.sortVertexBox();
        for (int i = 0; i <Vertex.vertexBox.size() ; i++) {
            theGraph.addVertex(Vertex.vertexBox.get(i));
        }
   theGraph.addEdge(v0.index,v1.index,50);
        theGraph.addEdge(v0.index,v3.index,80);   //0,4,70
        theGraph.addEdge(v1.index,v2.index,60);   //1,2,60/
        theGraph.addEdge(v1.index,v3.index,90);
        theGraph.addEdge(v2.index,v4.index,40);
        theGraph.addEdge(v3.index,v2.index,20);
        theGraph.addEdge(v3.index,v4.index,70);
        theGraph.addEdge(v4.index,v1.index,50);

        theGraph.addEdge(v1.index,v0.index,50);
        theGraph.addEdge(v3.index,v0.index,80);   //0,4,70
        theGraph.addEdge(v2.index,v1.index,60);   //1,2,60/
        theGraph.addEdge(v3.index,v1.index,90);
        theGraph.addEdge(v4.index,v2.index,40);
        theGraph.addEdge(v2.index,v3.index,20);
        theGraph.addEdge(v4.index,v3.index,70);
        theGraph.addEdge(v1.index,v4.index,50);

        theGraph.printAdjMat();

*/
        //Считываем табличку
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";         // используем запятую как разделитель
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
            String[] techList = line.split(cvsSplitBy);
                System.out.println(techList[0]+"\t" + techList[1] + "\t" + techList[2]);//Выводим список вершин и полей с техникой

                for (Vertex vert : Vertex.vertexBox) {
                    if (vert.label.equals(techList[2])){ vert.addMachineryOnField(techList[1]);}
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        //Ждем ввода начальной точки - - передаем в качестве аргумента

        //Перебираем графы и если данная точка принадлежит графу ищем ближайшие точки,
        //откуда может прибыть техника нужного вида

        //Выводим точки

        //Ждем команды



        System.out.println();
        theGraph.path();
        System.out.println();

    }

    public static void main(String[] args) {
        String filePath = args[0];
        String strStPoint = args[1];
        initProgram(filePath,Integer.valueOf(strStPoint));
    }
}
