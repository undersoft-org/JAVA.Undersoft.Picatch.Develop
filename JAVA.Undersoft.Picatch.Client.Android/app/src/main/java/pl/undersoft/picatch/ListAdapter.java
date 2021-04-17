package pl.undersoft.picatch;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter<Model> extends ArrayAdapter {
	
	//private View selectedView = null;
	private boolean selectedSomething = false;
	private TextView stt1 = null;
	private TextView stt2 = null;
	private TextView stt3 = null;
	private TextView stt4 = null;
	public String selectedId = "";

//	TextView att1 = null;
//	TextView att2 = null;
//	TextView att3 = null;
//	TextView att4 = null;

    @SuppressWarnings("unchecked")
	public ListAdapter(Context context, int resource, int textViewResourceId, ArrayList<PicatchModel> items) {
    	super(context, resource, textViewResourceId, items);
    }

//private static class ViewHolder{
//	String zuzu;
//}
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
//ViewHolder h;
        PicatchModel p = (PicatchModel)getItem(position);
//        System.out.println("position : " + position);
        
        try {
	        if (v == null) {
	        	//System.out.println("a1");
	            LayoutInflater vi;
	            vi = LayoutInflater.from(getContext());
	            //v = vi.inflate(R.layout.doc_list_layout, null);
	            //podobno uzywane gdy chcemy uzywac parametrow layouta 
	            v = vi.inflate(R.layout.doc_list_layout, parent, false);
	            //System.out.println("a2");
//h = new ViewHolder();
//				h.zuzu = "zu";
//				v.setTag(h);
	            //v.setTag(p.getId());
	        }
		} catch(Exception ex1) {
			System.out.println("ex1 : " + ex1.toString());
		}
        
        //System.out.println("a3");
        try {
	        if (p != null) {
	        	//System.out.println("a4");
	        	v.setTag(p.getId());
	        	
	        	TextView tt1 = (TextView) v.findViewById(R.id.editText1_l_1);
	        	TextView tt2 = (TextView) v.findViewById(R.id.editText1_l_3);
	        	TextView tt3 = (TextView) v.findViewById(R.id.editText1_l_2);
	        	TextView tt4 = (TextView) v.findViewById(R.id.editText1_l_4);
	            try {
//	            	if(p.getId().equals(selectedId)) {
//	            		System.out.println("equals");
//	            	} else {
//	            		System.out.println(" not equals");
//	            	}
//	            	if(tt1.isSelected()) {
//	            		System.out.println(" isSelected");
//	            	}
//	            	if(tt1.isSelected() && p.getId().equals(selectedId)) {
	            	if(p.getId().equals(selectedId)) {
//	            		System.out.println("   on");
		        		if(stt1 != null) {
		        			stt1.setSelected(false);
		        		}
		        		if(stt2 != null) {
		        			stt2.setSelected(false);
		        		}
		        		if(stt3 != null) {
		        			stt3.setSelected(false);
		        		}
		        		if(stt4 != null) {
		        			stt4.setSelected(false);
		        		}
	            		tt1.setSelected(true);
	            		tt2.setSelected(true);
	            		tt3.setSelected(true);
	            		tt4.setSelected(true);
	            		stt1 = tt1;
	            		stt2 = tt2;
	            		stt3 = tt3;
	            		stt4 = tt4;
	            	} else {
//	            		System.out.println("   off");
	            		tt1.setSelected(false);
	            		tt2.setSelected(false);
	            		tt3.setSelected(false);
	            		tt4.setSelected(false);
	            	}
	            	//v.setTag(p.getId());
//	            	System.out.println("v:p.getId() = " + p.getId() + ", selId = " + selectedId);
				} catch(Exception ex2) {
					System.out.println("v == null : Id = " + p.getId());
				    System.out.println("ex2 : " + ex2.toString());
				}
	            try {
	            	if (tt1 != null) {
	            		tt1.setText(p.getName());
	            	}
				} catch(Exception ex2) {
				    System.out.println("ex2 : " + ex2.toString());
				}
				try {
		            if (tt2 != null) {
		                tt2.setText(p.getTyp());
		            }
				} catch(Exception ex3) {
				    System.out.println("ex3 : " + ex3.toString());
				}
				try {
		            if (tt3 != null) {
		                tt3.setText(p.getData());
		            }
				} catch(Exception ex4) {
					System.out.println("ex4 : " + ex4.toString());
				}
				try {            
		            if (tt4 != null) {
		                tt4.setText(p.getSend());
		            }
				} catch(Exception ex5) {
				 	System.out.println("ex5 : " + ex5.toString());
				}
	        }
		} catch(Exception ex5) {
		    System.out.println("ex5 : " + ex5.toString());
		}
		//System.out.println("a5");
        
//		v.setOnItemClickListener(new OnItemClickListener() {
//    	public void onItemClick(AdapterView<?> parent, View v,
//    	    int position, long id) {
//    		System.out.println("position : " + position);
//    		System.out.println("id : " + id);
////    	        Toast.makeText(getApplicationContext(),
////    	        		((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//    	    }
//    	});
        
		v.setOnTouchListener(new View.OnTouchListener() {
        	TextView att1 = null;
        	TextView att2 = null;
        	TextView att3 = null;
        	TextView att4 = null;
        	
        	@Override
            public boolean onTouch(View v, MotionEvent event) {
            	//System.out.println("*******    onTouch");
            	
	        	if( event.getAction() == MotionEvent.ACTION_DOWN
//	        		||event.getAction() == MotionEvent.ACTION_UP
	        		) {
	        		//selectedView = v;
	        		selectedId = (String)v.getTag();
//	        		System.out.println("selectedId = " + selectedId);
		        	att1 = (TextView) v.findViewById(R.id.editText1_l_1);
		        	att2 = (TextView) v.findViewById(R.id.editText1_l_2);
		        	att3 = (TextView) v.findViewById(R.id.editText1_l_3);
		        	att4 = (TextView) v.findViewById(R.id.editText1_l_4);
		        	
		        	if(selectedSomething) {
		        		//selectedSomething = false;
		        		if(stt1 != null) {
		        			stt1.setSelected(false);
		        		}
		        		if(stt2 != null) {
		        			stt2.setSelected(false);
		        		}
		        		if(stt3 != null) {
		        			stt3.setSelected(false);
		        		}
		        		if(stt4 != null) {
		        			stt4.setSelected(false);
		        		}
		        	} else {
		        		selectedSomething = true;
		        	}
                	if(att1 != null) {
                		stt1 = att1;
                		stt1.setSelected(true);
                	}
                	if(att2 != null) {
                		stt2 = att2;
                		stt2.setSelected(true);
                	}
                	if(att3 != null) {
                		stt3 = att3;
                		stt3.setSelected(true);
                	}
                	if(att4 != null) {
                		stt4 = att4;
                		stt4.setSelected(true);
                	}		        		
		        	return true;
	        	} else if( event.getAction() == MotionEvent.ACTION_UP ) {
	        		return true;
	        	}
/*	        	
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                    	//v.setSelected(true);
                    	if(tt1 != null) {
                    		tt1.setSelected(true);
                    	}
                    	if(tt2 != null) {
                    		tt2.setSelected(true);
                    	}
                    	if(tt3 != null) {
                    		tt3.setSelected(true);
                    	}
                    	if(tt4 != null) {
                    		tt4.setSelected(true);
                    	}
                    	System.out.println("*******    ACTION_DOWN");
                    	//scanningEnable();
                    	try {
//                    		System.out.println("*******    onTouch v.getTag() = " + (String)v.getTag());
//                    		System.out.println("*******    onTouch v.getId() = " + v.getId());
//                    		System.out.println("*******    onTouch v.getX() = " + v.getX());
//                    		System.out.println("*******    onTouch v.getY() = " + v.getY());
                    	} catch(Exception ex1) {
                    		System.out.println("*******    onTouch ex1 : " + ex1.toString());
                    	}
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                    	//v.setSelected(false);
                    	if(tt1 != null) {
                    		tt1.setSelected(false);
                    	}
                    	if(tt2 != null) {
                    		tt2.setSelected(false);
                    	}
                    	if(tt3 != null) {
                    		tt3.setSelected(false);
                    	}
                    	if(tt4 != null) {
                    		tt4.setSelected(false);
                    	}
                    	System.out.println("*******    ACTION_UP");
                    	//scanningDisable();
                    	try {
//                    		System.out.println("*******    onTouch v.getTag() = " + (String)v.getTag());
//                    		System.out.println("*******    onTouch v.getId() = " + v.getId());
//                    		System.out.println("*******    onTouch v.getX() = " + v.getX());
//                    		System.out.println("*******    onTouch v.getY() = " + v.getY());
                    	} catch(Exception ex1) {
                    		System.out.println("*******    onTouch ex1 : " + ex1.toString());
                    	}
                        return true; // if you want to handle the touch event
                }
*/
                return false;
            }
        });
        
        return v;
    }
}
