package org.androidtown.multimemo;


public class MemoListItem {


	/**
	 * Data array
	 */
	private String[] mData;

	/**
	 * Item ID
	 */
	private String mId;

	/**
	 * True if this item is selectable
	 */
	private boolean mSelectable = true;

	/**
	 * Initialize with icon and data array
	 *
	 * @param obj
	 *
	 */
	public MemoListItem(String itemId, String[] obj) {
		mId = itemId;
		mData = obj;
	}

	/**
	 * Initialize with strings
	 *
	 *
	 * @param obj01 - memo input_date
	 * @param obj02 - memo memoStr
	 * @param obj03 - memo picture_id
	 *
	 */
	public MemoListItem(String memoId, String memoDate, String memoText,
			String id_handwriting, String uri_handwriting,
			String id_photo, String uri_photo,
			String id_video, String uri_video,
			String id_voice, String uri_voice
			)
	{
		mId = memoId;
		mData = new String[10];
		mData[0] = memoDate;
		mData[1] = memoText;
		mData[2] = id_handwriting;
		mData[3] = uri_handwriting;
		mData[4] = id_photo;
		mData[5] = uri_photo;
		mData[6] = id_video;
		mData[7] = uri_video;
		mData[8] = id_voice;
		mData[9] = uri_voice;
	}

	/**
	 * True if this item is selectable
	 */
	public boolean isSelectable() {
		return mSelectable;
	}

	/**
	 * Set selectable flag
	 */
	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}

	public String getId() {
		return mId;
	}

	public void setId(String itemId) {
		mId = itemId;
	}


	/**
	 * Get data array
	 *
	 * @return
	 */
	public String[] getData() {
		return mData;
	}

	/**
	 * Get data
	 */
	public String getData(int index) {
		if (mData == null || index >= mData.length) {
			return null;
		}

		return mData[index];
	}

	/**
	 * Set data array
	 *
	 * @param obj
	 */
	public void setData(String[] obj) {
		mData = obj;
	}


	/**
	 * Compare with the input object
	 *
	 * @param other
	 * @return
	 */
	public int compareTo(MemoListItem other) {
		if (mData != null) {
			Object[] otherData = other.getData();
			if (mData.length == otherData.length) {
				for (int i = 0; i < mData.length; i++) {
					if (!mData[i].equals(otherData[i])) {
						return -1;
					}
				}
			} else {
				return -1;
			}
		} else {
			throw new IllegalArgumentException();
		}

		return 0;
	}

}
