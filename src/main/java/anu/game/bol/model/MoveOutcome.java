package anu.game.bol.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class MoveOutcome {
    private  int[] beadCount;
    private  boolean anotherGo;
    
    public MoveOutcome(int[] beadCountp, boolean anotherGop) {
    	beadCount = beadCountp;
    	anotherGo = anotherGop;
    }
    
	public int[] getBeadCount() {
		return beadCount;
	}
	public void setBeadCount(int[] beadCount) {
		this.beadCount = beadCount;
	}
	public boolean isAnotherGo() {
		return anotherGo;
	}
	public void setAnotherGo(boolean anotherGo) {
		this.anotherGo = anotherGo;
	}
	
	@Override
	public String toString() {
		return beadCount.toString() + " " + anotherGo;
		
	}
	
}
