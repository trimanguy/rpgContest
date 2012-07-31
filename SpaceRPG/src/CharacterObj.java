/**
 * @(#)CharacterObj.java
 *
 *
 * @author 
 * @version 1.00 2012/7/31
 */


public class CharacterObj extends Obj{
	static String[] maleNames = new String[] {"Aaron", "Adam", "Adrian", "Alan", "Alejandro", "Alex", "Allen", "Andrew", "Andy", "Anthony", "Art", 
												"Arthur", "Barry", "Bart", "Ben", "Benjamin", "Bill", "Bobby", "Brad", "Bradley", "Brendan", "Brett", 
												"Brian", "Bruce", "Bryan", "Carlos", "Chad", "Charles", "Chris", "Christopher", "Chuck", "Clay", 
												"Corey", "Craig", "Dan", "Daniel", "Darren", "Dave", "David", "Dean", "Dennis", "Denny", "Derek", "Don", 
												"Doug", "Duane", "Edward", "Eric", "Eugene", "Evan", "Frank", "Fred", "Gary", "Gene", "George", "Gordon", 
												"Greg", "Harry", "Henry", "Hunter", "Ivan", "Jack", "James", "Jamie", "Jason", "Jay", "Jeff", "Jeffrey", 
												"Jeremy", "Jim", "Joe", "Joel", "John", "Jonathan", "Joseph", "Justin", "Keith", "Ken", "Kevin", "Larry", 
												"Logan", "Marc", "Mark", "Matt", "Matthew", "Michael", "Mike", "Nat", "Nathan", "Patrick", "Paul", "Perry", 
												"Peter", "Philip", "Phillip", "Randy", "Raymond", "Ricardo", "Richard", "Rick", "Rob", "Robert", "Rod", 
												"Roger", "Ross", "Ruben", "Russell", "Ryan", "Sam", "Scot", "Scott", "Sean", "Shaun", "Stephen", "Steve", 
												"Steven", "Stewart", "Stuart", "Ted", "Thomas", "Tim", "Toby", "Todd", "Tom", "Troy", "Victor", "Wade", 
												"Walter", "Wayne", "William" };
												
	static String[] femaleNames = new String[] {"Aimee", "Aleksandra", "Alice", "Alicia", "Allison", "Alyssa", "Amy", "Andrea", "Angel", "Angela", 
												"Ann", "Anna", "Anne", "Marie", "Annie", "Ashley", "Barbara", "Beatrice", "Beth", "Betty", "Bonnie", 
												"Brenda", "Brooke", "Candace", "Cara", "Caren", "Carol", "Caroline", "Carolyn", "Carrie", 
												"Cassandra", "Catherine", "Charlotte", "Chrissy", "Christen", "Christina", "Christine", 
												"Christy", "Claire", "Claudia", "Courtney", "Crystal", "Cynthia", "Dana", "Danielle", "Deanne", 
												"Deborah", "Deirdre", "Denise", "Diane", "Dianne", "Dorothy", "Eileen", "Elena", "Elizabeth", 
												"Ellen", "Emily", "Erica", "Erin", "Frances", "Gina", "Giulietta", "Heather", "Helen", "Jane", "Janet", "Janice", 
												"Jenna", "Jennifer", "Jessica", "Joanna", "Joyce", "Julia", "Juliana", "Julie", "Justine", "Kara", 
												"Karen", "Katie", "Katherine", "Kathleen", "Kathryn", "Katrina", "Kelly", "Kerry", "Kim", 
												"Kimberly", "Kristen", "Kristina", "Kristine", "Laura", "Laurel", "Lauren", "Laurie", "Leah", 
												"Linda", "Lindsey", "Lisa", "Lori", "Marcia", "Margaret", "Maria", "Marina", "Marisa", "Martha", "Mary", "Mary", 
												"Shannon", "Maya", "Melanie", "Melissa", "Michelle", "Monica", "Nancy", "Natalie", "Nicole", "Nina", 
												"Pamela", "Patricia", "Rachel", "Rebecca", "Renee", "Sandra", "Sara", "Sharon", "Sheri", "Shirley", 
												"Sonia", "Alice", "Stephanie", "Susan", "Suzanne", "Sylvia", "Tina", "Samantha", "Connie", "Terri", 
												"Theresa", "Tiffany", "Tracy", "Valerie", "Veronica", "Vicky", "Vivian", "Wendy"};
												 
	static String[] surNames = new String[]		{"Adams", "Barber", "Bolton", "Bragg", "Cox", "Cavallaro", "Leu", "Su", "Vasquez", "Nguyen", "Yang", "Gu", "Rollins", "Rogan", 
												"Steakly", "Stevens", "DeMuth", "Edmonds", "Roh", "Jacobs", "Jones", "Tully", "Sutton", "Wagner", "Young", "Ziggy", "Schmidt", 
												"Wright", "Moore", "Kim", "Whitman", "O'Connor", "O'Brien","Kane","Hopper","Steele", "Hawkes","Lawson", "Hansberg","Heisenberg",
												"Dovak", "Yee", "Huynh","Clarke","Xiao","Tanaka","Park","Caesar","Huffman", "Gomez", "Rodriguez","Harding","Halleck","Chen",
												"Lee","Kaiser","Williams","Viloria","Hussain","Rahimi","Rose"};
	
	String name;
	boolean gender=false; //true=male, false=female
	String job; //this person's class
	int gunnery=50; 	//bonus dmg
	int accuracy=50; 	//lowers fire spread
	int efficiency=50; //lowers power/ammo usage and shield recharge delay
	int dmgControl=50;	//increases life and maybe armor
	int calibration=50;//increases engine speed or pylon turn rate
	int engineering=50;//increases max shield or max power
	
	
    public CharacterObj() {
    	if(Utils.randomNumberGen(0,2)==1){gender=true;}
    	if(gender){
    		int fn=Utils.randomNumberGen(0,maleNames.length);
    		int ln=Utils.randomNumberGen(0,surNames.length);
    		name = maleNames[fn]+" "+surNames[ln];
    	} else {
    		int fn=Utils.randomNumberGen(0,femaleNames.length);
    		int ln=Utils.randomNumberGen(0,surNames.length);
    		name = femaleNames[fn]+" "+surNames[ln];
    	}
    	
    	int randomPts=180; //each stat will start at least >=50. These points are then randomly distributed
    	while(randomPts>0){
    	
	  		int picked = Utils.randomNumberGen(0,6);
	  		int quant = Math.min(Utils.randomNumberGen(3,14),randomPts);
	  		switch (picked) {
	    		case 0:		gunnery+=quant; randomPts-=quant; break;
	    		case 1:		accuracy+=quant; randomPts-=quant; break;
	    		case 2:		efficiency+=quant; randomPts-=quant; break;
	    		case 3:		dmgControl+=quant; randomPts-=quant; break;
	    		case 4:		calibration+=quant; randomPts-=quant; break;
	    		case 5:		engineering+=quant; randomPts-=quant; break;
	  		}
    	}
    	
    	int maxStat=Math.max(gunnery, Math.max(accuracy,Math.max(efficiency,Math.max(dmgControl,Math.max(calibration,engineering)))));
		if(maxStat==gunnery) job="gunner";
    	if(maxStat==accuracy)		job="sniper";
    	if(maxStat==efficiency) 	job="logistics officer";
    	if(maxStat==dmgControl) 	job="technician";
    	if(maxStat==calibration) 	job="calibrator";
    	if(maxStat==engineering) 	job="engineer";
    	
    	
    	System.out.println(job+" " +name+ " made! With stats: gunnery: "+gunnery+" accuracy: "
    		+accuracy+" efficiency: " +efficiency+" dmgControl: "+dmgControl+" calibration: "+calibration+" enginnering: "+engineering);
	}
}
