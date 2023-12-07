package aptg.bean;

import java.io.Serializable;

import aptg.model.NBAllocationHisModel;
import aptg.model.NBListModel;

public class AllocationNBListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private NBAllocationHisModel nbAllocatin;
	private NBListModel nbList;
	
	public NBAllocationHisModel getNbAllocatin() {
		return nbAllocatin;
	}
	public void setNbAllocatin(NBAllocationHisModel nbAllocatin) {
		this.nbAllocatin = nbAllocatin;
	}
	public NBListModel getNbList() {
		return nbList;
	}
	public void setNbList(NBListModel nbList) {
		this.nbList = nbList;
	}
}
