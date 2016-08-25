import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wikipedia.miner.model.Article;
import org.wikipedia.miner.model.Category;
import org.wikipedia.miner.model.Page;
import org.wikipedia.miner.model.Wikipedia;
import org.wikipedia.miner.util.WikipediaConfiguration;






public class WikipediaDefiner {
	
	static BufferedWriter out;
	BufferedReader _input;
	Cleaner c = new Cleaner();
	
	private static String ambilTextAnnot(String ask, String desc) throws IOException{
		String query = ask;
		String hasil = null;
		Cleaner c = new Cleaner();
		
		if(desc != null){
			if(c.readyAnnotate(query, desc)){
				
				hasil = c.setAnnotate(query, desc);
			}
		}
		
		return hasil;
	}
	
    public static void main(String args[]) throws Exception {

        WikipediaConfiguration conf = new WikipediaConfiguration(new File("X:/Database Berkeley/wikipedia-miner-1.2.0/wikipedia-template.xml")) ;

        Wikipedia wikipedia = new Wikipedia(conf, false) ;
        Cleaner c = new Cleaner();

        Article article = wikipedia.getArticleByTitle("Banjir Asia Tenggara dan Asia Selatan 2014–15") ;//Banjir Asia Tenggara dan Asia Selatan 2014–15
//        System.out.println(article.getMarkup()) ;
        
        //Page page = wikipedia.getPageById(44);//616118
        //Article article = wikipedia.getArticleByTitle(page.getTitle());
        System.out.println(article.getId());
        System.out.println(article.getType());
        System.out.println(article.getTitle());
        //System.out.println(article.getMarkup());
        
        
        out = new BufferedWriter(new FileWriter("X:/Database Berkeley/Output/annotasi/markupProvince - "+article.getTitle()+".txt"));
		out.write(article.getMarkup());
		out.newLine();
		out.close();
        
        /// WORKSPACE ///
        
        //test page link in // 
        
        Article[] setLinkInArtikel = article.getLinksIn();
        Article[] setLinkOutArtikel = article.getLinksOut();
        
//        if (setLinkInArtikel.length == 0) {
//     	   System.out.println("\n!! Kategori ini tidak mempunyai link in artikel !!");
//        } else {
//     	   System.out.println("=== Link in artikel untuk Kategori ini yaitu : ===");
//            for (int i=0 ; i<setLinkInArtikel.length ; i++) { 
//                System.out.println(" - [" + (i+1) + "] " + setLinkInArtikel[i].getTitle() + "\n") ;
//            }
//            
//        }
        
        String penampung = null;
        List<tempBox> label = c.getBox2(article);
        List<tempBox> labelOut = null;
        String akhir = c.getDesc(article);
        
        //test box//
        
        
        System.out.print("jumlah box yang tertangkap " + label.size() + "\n");
        for (int j = 0; j<label.size(); j++){ 
        	System.out.print("jumlah label yang di dapat dari box " + (j+1) + " adalah " +label.get(j).getLabel().size() + "\n");
   		 for (int k = 0; k<label.get(j).getLabel().size(); k++){ 
   			
   			 
   			System.out.print(label.get(j).getLabel().get(j));
   			System.out.print("            " + label.get(j).getValue().get(j) +  "\n");
   			 
   		 }

   	 }
        
        ////
        
        //System.out.println(c.getBox(article));
        
        //System.out.println(akhir);
        
        if (setLinkOutArtikel.length == 0) {
      	   System.out.println("\n!! Kategori ini tidak mempunyai link out artikel !!");
         } else {
      	   System.out.println("=== link out ditemukan dans sedang di proses anotasi train nya : ===");
             for (int i=0 ; i<setLinkOutArtikel.length ; i++) { 
            	 System.out.println(" - [" + (i+1) + "] " + setLinkOutArtikel[i].getTitle()+ "\n") ;
            	 
            	 // check apakah link out tersebut adalah negara atau bukan
            	 if(c.isCountry(setLinkOutArtikel[i])){
            		 
            		 penampung = ambilTextAnnot(setLinkOutArtikel[i].getTitle(), akhir);
            		 if(penampung != null)
            			akhir = penampung;
            		 	System.out.println(setLinkOutArtikel[i].getTitle()+ " merupakan negara\n");
                		 
                //else linkOut wasn't a country, then check for it's infobox. if there any label named  location
                	 
            	 }else if(c.containHeaderAnnot(setLinkOutArtikel[i])){
            		 penampung = ambilTextAnnot(setLinkOutArtikel[i].getTitle(), akhir);
            		 if(penampung != null)
            			akhir = penampung;
            		 	System.out.println(setLinkOutArtikel[i].getTitle()+ " terdapat header yang dicari\n");
            		 	
            	 }else{
            	 
	            	 labelOut = c.getBox2(setLinkOutArtikel[i]);
	            	 
	            	 for (int j = 0; j<labelOut.size(); j++){
	            		 //System.out.print(labelOut.size()+"\n");
	            		 	            		 
	            			 if (labelOut.get(j).getLabel().contains("location")||labelOut.get(j).getLabel().contains("capital")||labelOut.get(j).getLabel().contains("penduduk")||labelOut.get(j).getLabel().contains("region")||labelOut.get(j).getLabel().contains("luas")||labelOut.get(j).getLabel().contains("peta")){
	            				 
	            				 penampung = ambilTextAnnot(setLinkOutArtikel[i].getTitle(), akhir);
	                        	 if(penampung != null)
	                        		 akhir = penampung;		 
	                        	 System.out.print(setLinkOutArtikel[i].getTitle()+ " merupakan label yang harus diubah\n");
	            			 }
	            			 
	            		 for (int k = 0; k<labelOut.get(j).getLabel().size(); k++){ 
	            			 
	            			 Pattern a = Pattern.compile("population_[a-z]+|population");
	            			 Pattern b = Pattern.compile("area_[a-z]+|area");
	            		     Matcher m = a.matcher(labelOut.get(j).getLabel().get(j));
	            		     Matcher n = b.matcher(labelOut.get(j).getLabel().get(j));
	            		     
	            		     if(m.find()||n.find()){
	            		    	 penampung = ambilTextAnnot(setLinkOutArtikel[i].getTitle(), akhir);
	                        	 if(penampung != null)
	                        		 akhir = penampung;		 
	                        	 System.out.print(setLinkOutArtikel[i].getTitle()+ " merupakan label yang harus diubah\n");
	            		     }
	            		     
	            			 
	            		 }
		 
	            	 }
            	 }
            	 
            	 
             }
             
         }
        
//        if(akhir!= null)
//   		 System.out.println(akhir);
        
        out = new BufferedWriter(new FileWriter("X:/Database Berkeley/Output/annotasi/result/cobaLagi.txt"));
		out.write(akhir);
		out.newLine();
		out.close();
        
        
        
        
        //////////////////////////////////////////////////
        
//        Category kategori = wikipedia.getCategoryByTitle("Artis Kamen Rider");
//        Article artikel = wikipedia.getArticleByTitle("Kesultanan Melaka");
//        //Category kategori = wikipedia.getRootCategory();
//        
//        if(kategori.contains(artikel)){
//        	System.out.println("BERHASIL");
//        }
//        
//        
//        if(kategori != null){
//        	
//        	System.out.println("- [Id Kategori] - " + kategori.getId());
//        	System.out.println("- [Nama Kategori] - " + kategori.getTitle() +"\n");
//        }
//        
//       Article[] tempArticle = kategori.getChildArticles(); 
//       Category[] tempChild  = kategori.getChildCategories();
//       Category[] tempParent = kategori.getParentCategories();
//       
//       if (tempArticle.length == 0) {
//    	   System.out.println("\n!! Kategori ini tidak mempunyai turunan artikel !!");
//       } else {
//    	   System.out.println("=== Turunan artikel untuk Kategori ini yaitu : ===");
//           for (int i=0 ; i<tempArticle.length ; i++) { 
//               System.out.println(" - [" + (i+1) + "] " + tempArticle[i].getTitle()) ;
//           }
//           
//       }
//       
//       System.out.println("\n") ;
//       
//       if (tempChild.length == 0) {
//    	   System.out.println("\n!! Kategori ini tidak mempunyai turunan kategori !!");
//       } else {
//    	   System.out.println("=== Kategori Turunan untuk Kategori ini yaitu : ===");
//           for (int i=0 ; i<tempChild.length ; i++) { 
//               System.out.println(" - [" + (i+1) + "] " + tempChild[i].getTitle()) ;
//           }
//           
//       }
//       
//       System.out.println("\n") ;
//       
//       if (tempParent.length == 0) {
//    	   System.out.println("!! Kategori ini tidak mempunyai parent kategori !!");
//       } else {
//    	   System.out.println("=== Kategori parent untuk Kategori ini yaitu : ===");
//           for (int i=0 ; i<tempParent.length ; i++) {
//        	   
//               System.out.println(" - [" + (i+1) + "] " + tempParent[i].getTitle()) ;
//           }
//           
//       }
//       
        wikipedia.close() ;
    }
    
}