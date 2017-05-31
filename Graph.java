import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Graph {

    private final int MAX_VERTS = 5;
    private final int INFINITY = 200;
    private Vertex vertexList[]; // Список вершин
    private int adjMat[][];      // Матрица смежности
    private int adjMatShift[][];
    private int nVerts;          // Текущее количество вершин
    private int nTree;           // Количество вершин в дереве
    private DistPar sPath[];     // Массив данных кратчайших путей
    private int currentVert;     // Текущая вершина
    private int startToCurrent;  // Расстояние до currentVert
    private Map<String,Integer> resultBox = new TreeMap<>();
    public static boolean ASC = true;
    public static boolean DESC = false;

    public Graph() {

        vertexList = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS];
        adjMatShift = new int[MAX_VERTS][MAX_VERTS];// Матрица смежности
        nVerts = 0;
        nTree = 0;
        for(int j=0; j<MAX_VERTS; j++) {
            for (int k = 0; k < MAX_VERTS; k++) {  // заполняется
                adjMatShift[j][k] = INFINITY;
                adjMat[j][k] = INFINITY;
            }
        }
        sPath = new DistPar[MAX_VERTS];     // Массив кратчайших путей
    }

    public void addVertex(Vertex vert)
    {
        vertexList[nVerts++] = vert;
    }

    public void addEdge(int start, int end, int weight)
    {
        adjMatShift[start][end] = weight;
    }

    public void printAdjMat(){
        for (int i = 0; i <adjMat.length ; i++) {
            for (int j = 0; j <adjMat.length ; j++) {
                System.out.print(adjMat[i][j]+"\t"+"\t");
            }
            System.out.println();
        }

    }

    public void printAdjMatShift(){
        for (int i = 0; i <adjMatShift.length ; i++) {
            for (int j = 0; j <adjMatShift.length ; j++) {
                System.out.print(adjMatShift[i][j]+"\t"+"\t");
            }
            System.out.println();
        }

    }

    public void changeAddMat(int startPoint){

        for (int i = 0; i <adjMat.length ; i++) {
            for (int j = 0; j <adjMat.length ; j++) {
               if((i+startPoint<adjMat.length) && (j+startPoint<adjMat.length)){
                   adjMat[i][j] = adjMatShift[i+ startPoint][j+startPoint];
                }
                if((i+startPoint>=adjMat.length) && (j+startPoint>=adjMat.length)){
                    adjMat[i][j] = adjMatShift[i+ startPoint-adjMat.length][j+startPoint-adjMat.length];
                }
                if((i+startPoint<adjMat.length) && (j+startPoint>=adjMat.length)){
                    adjMat[i][j] = adjMatShift[i+ startPoint][j+startPoint-adjMat.length];
                }
                if((i+startPoint>=adjMat.length) && (j+startPoint<adjMat.length)){
                    adjMat[i][j] = adjMatShift[i+ startPoint-adjMat.length][j+startPoint];
                }

            }


        }

    }

    public void path()  // Выбор кратчайших путей
    {
        int startTree = 0;                                      // Начиная с вершины 0
        vertexList[startTree].setInTreeTrue();                  // Включение в дерево
        nTree = 1;                                              // Перемещение строки расстояний из adjMat в sPath

        for(int j=0; j<nVerts; j++)
        {
            int tempDist = adjMat[startTree][j];
            sPath[j] = new DistPar(startTree, tempDist);
        }

        while(nTree < nVerts){                                   // Пока все вершины не окажутся в дереве
            int indexMin = getMin();                            // Получение минимума из sPath
            int minDist = sPath[indexMin].distance;
            if(minDist == INFINITY) {                           // Если все расстояния бесконечны
                System.out.println("There are unreachable vertices");
                break;
            }
            else {
                currentVert = indexMin; // Возврат к ближайшей вершине
                startToCurrent = sPath[indexMin].distance;
            }
            vertexList[currentVert].setInTreeTrue();// Включение текущей вершины в дерево
            nTree++;
            adjust_sPath();         // Обновление массива sPath[]
        }
        displayPaths();             // Вывод содержимого sPath[]
        nTree = 0;
        for(int j=0; j<nVerts; j++)     // Очистка дерева
            vertexList[j].setInTreeFalse();
    }
    // -------------------------------------------------------------
    public int getMin() {

        int minDist = INFINITY;     // Исходный высокий "минимум"
        int indexMin = 0;

        for(int j=1; j<nVerts; j++) // Для каждой вершины
        {
            if( !vertexList[j].isInTree() &&        // Если она не включена в дерево  и ее расстояние меньше
                    sPath[j].distance < minDist )   // старого минимума
            {
                minDist = sPath[j].distance;
                indexMin = j;
            }
        }
        return indexMin;        // Обновление минимума
    }

    public void adjust_sPath() {

        int column = 1;
        while(column < nVerts)          // Перебор столбцов
        {
            if( vertexList[column].isInTree ) {      // Если вершина column уже включена в дерево, она пропускается
                column++;
                continue;
            }

            int currentToFringe = adjMat[currentVert][column];           // Получение ребра от currentVert к column
            int startToFringe = startToCurrent + currentToFringe;       // Суммирование расстояний
            int sPathDist = sPath[column].distance;                     // Определение расстояния текущего элемента sPath
            if(startToFringe < sPathDist) {                               // Сравнение расстояния от начальной вершины с элементом sPath  Если меньше,
                sPath[column].parentVert = currentVert;             // данные sPath обновляются
                sPath[column].distance = startToFringe;
            }
            column++;
        }
    }

    public void displayPaths()
    {
        for(int j=0; j<nVerts; j++) // display contents of sPath[]
        {
            System.out.print(vertexList[j].label + "="); // B=
            resultBox.put(vertexList[j].label,sPath[j].distance);
            if(sPath[j].distance == INFINITY) System.out.print("inf");
            else System.out.print(sPath[j].distance);

            String parent = vertexList[ sPath[j].parentVert ].label;
            System.out.println("(" + parent + ")");
        }
        System.out.println();
    }

    public void displayNearestMachinery(){
            int numberOfMachinery=0;
        for (int i = 0; i <nVerts ; i++) {
            if(vertexList[i].index==0) {
                String requestType = vertexList[i].machineryType;
                System.out.println("Требуемый тип техники - " + requestType);
                    for (String type:vertexList[i].machineryOnField) {
                        if(type.equals(requestType)) System.out.print(vertexList[i].label + " - "+ sPath[i].distance + requestType+" "+i);
                }
            }
            Map<String, Integer> sortedMapAsc = sortByComparator(resultBox, ASC);
            for (Entry<String, Integer> entry : resultBox.entrySet())
            {
                System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
            }
            for (int j = 0; j <nVerts ; j++) {

         }

            }





    }

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
