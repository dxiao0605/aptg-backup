import React from "react";

const IMPTypeUnit = ({IMPType}) => {
	return (
		<div className='unit'>
			{(() => {
				switch (IMPType) {
					case 20:
						return "[UΩ]";
					case 21:
						return "[mΩ]";
					case 22:
						return "[S]";
					default:
						return "[UΩ]";
				}
			})()}
		</div>
	);
};
export default IMPTypeUnit;
