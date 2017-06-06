import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


public class Graph {

    private final int MAX_VERTS = 5;
    private final int INFINITY = 200;
    private Vertex vertexList[];                            // Список вершин
    private int adjMat[][];                                 // Матрица смежности
    private int adjMatShift[][];
    private int nVerts;                                     // Текущее количество вершин
    private int nTree;                                      // Количество вершин в дереве
    private DistPar sPath[];                                // Массив данных кратчайших путей
    private int currentVert;                                // Текущая вершина
    private int startToCurrent;                             // Расстояние до currentVert
    private Map<Vertex,Integer> resultBox = new HashMap<>();//В нем хранится вершина и расстояние до нее от стартовой точки
    public static boolean ASC = true;                       //Cортировка в восходящем порядке
    public static boolean DESC = false;                     //Cортировка в нисходящем порядке

    public Graph() {

        vertexList = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS];     // Матрица смежности
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
     //   displayPaths();// Вывод содержимого sPath[]
        displayNearestMachinery();
        resultBox.clear();
        for(int j=0; j<MAX_VERTS; j++) {
            for (int k = 0; k < MAX_VERTS; k++) {  // заполняется
              //  adjMatShift[j][k] = INFINITY;
                adjMat[j][k] = INFINITY;
            }

        }
        nTree = 0;
        for(int j=0; j<nVerts; j++)     // Очистка дерева
            vertexList[j].setInTreeFalse();
            nVerts=0;

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

            if(sPath[j].distance == INFINITY) System.out.print("inf");
            else System.out.print(sPath[j].distance);

            String parent = vertexList[ sPath[j].parentVert ].label;
            System.out.println("(" + parent + ")");
        }
        System.out.println();
    }

    public void displayNearestMachinery() {
        String requestType = "";
        int numberOfMachinery = 0;
        for (int i = 0; i < nVerts; i++) {
            if (vertexList[i].index == 0) {
                requestType = vertexList[i].machineryType;
                System.out.println("Требуемый тип техники - " + requestType);
            }
        }
        System.out.println("Имя поля" + "\t"+"Расстояние" + "\t" + "Тип техники" + "\t" + "номер");
        System.out.println("###########################################################");
            for (int j = 0; j < nVerts; j++) {
                if(numberOfMachinery>=3) break;
                    if ((sPath[j].distance == INFINITY) && (vertexList[j].index != 0)) {    //если путь недостижим
                        System.out.println("Нет пути из точки " + vertexList[j].label);
                    }
                    if ((sPath[j].distance == INFINITY) && (vertexList[j].index == 0)) {    //если точка и есть начальная
                     //   System.out.println("Старотовая точка " + vertexList[j].label);
                        for (String type : vertexList[j].machineryOnField) {
                            if (type.equals(requestType)) {
                                System.out.println(vertexList[j].label + "\t\t" + "Начало" +	"\t\t"+ requestType + "\t\t\t " + numberOfMachinery);
                                numberOfMachinery++;
                            }
                        }
                    } else {                                                              //все остальные точки добавляем в контейнер
                        resultBox.put(vertexList[j], sPath[j].distance);
                    }
                }

                Map<Vertex, Integer> sortedMapAsc = sortByComparator(resultBox, ASC);

                for (Entry<Vertex, Integer> entry : sortedMapAsc.entrySet()) {
                    if(numberOfMachinery>=3) break;

                    for (String type : entry.getKey().machineryOnField) {
                        if(numberOfMachinery>=3) break;
                        if (type.equals(requestType)) {
                            System.out.println(entry.getKey().label + "\t\t" + entry.getValue() + "\t\t\t"+requestType + "\t\t\t" + numberOfMachinery);
                            numberOfMachinery++;
                        }
                      //  System.out.println("Key : " + entry.getKey().label + " Value : " + entry.getValue());
                    }

                }

    }

    private static Map<Vertex, Integer> sortByComparator(Map<Vertex, Integer> unsortMap, final boolean order){

        List<Entry<Vertex, Integer>> list = new LinkedList<Entry<Vertex, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Vertex, Integer>>()
        {
            public int compare(Entry<Vertex, Integer> o1,
                               Entry<Vertex, Integer> o2)
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
        Map<Vertex, Integer> sortedMap = new LinkedHashMap<Vertex, Integer>();
        for (Entry<Vertex, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
