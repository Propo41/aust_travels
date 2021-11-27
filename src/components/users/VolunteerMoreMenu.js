import { Icon } from "@iconify/react";
import { useRef, useState } from "react";
import { Link as RouterLink } from "react-router-dom";
import moreVerticalFill from "@iconify/icons-eva/more-vertical-fill";
// material
import { Menu, MenuItem, IconButton, ListItemText } from "@mui/material";

// ----------------------------------------------------------------------

export default function VolunteerMoreMenu(props) {
  const ref = useRef(null);
  const [isOpen, setIsOpen] = useState(false);

  const onDeleteClick = (e) => {
    e.preventDefault();
    setIsOpen(false);
    props.onDeleteClick(props.id);
  };

  const onApproveClick = (e) => {
    e.preventDefault();
    setIsOpen(false);
    props.onApproveClick(props.id);
  };

  return (
    <>
      <IconButton ref={ref} onClick={() => setIsOpen(true)}>
        <Icon icon={moreVerticalFill} width={20} height={20} />
      </IconButton>

      <Menu
        open={isOpen}
        anchorEl={ref.current}
        onClose={() => setIsOpen(false)}
        PaperProps={{
          sx: { width: 200, maxWidth: "100%" },
        }}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        transformOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <MenuItem sx={{ color: "text.secondary" }}>
          <ListItemText
            primary="Delete"
            primaryTypographyProps={{ variant: "body2" }}
            onClick={onDeleteClick}
          />
        </MenuItem>

        <MenuItem
          component={RouterLink}
          to="#"
          sx={{ color: "text.secondary" }}
        >
          <ListItemText
            primary="Approve"
            onClick={onApproveClick}
            primaryTypographyProps={{ variant: "body2" }}
          />
        </MenuItem>
      </Menu>
    </>
  );
}
