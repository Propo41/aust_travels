import { filter } from "lodash";
import { Icon } from "@iconify/react";
import { sentenceCase } from "change-case";
import { useEffect, useState } from "react";
import plusFill from "@iconify/icons-eva/plus-fill";
import { Link as RouterLink } from "react-router-dom";
// material
import {
  Card,
  Table,
  Stack,
  Avatar,
  Button,
  TableRow,
  TableBody,
  TableCell,
  Container,
  Typography,
  TableContainer,
  TablePagination,
} from "@mui/material";
// components
import Page from "components/Page";
import Label from "components/Label";
import { UserListHead, UserMoreMenu } from "components/users";

import Appbar from "components/Appbar";
import UserListToolbar from "components/users/UserListToolbar";
import { useAuth } from "auth/firebaseAuth";

// ----------------------------------------------------------------------
import { getDatabase, ref, onValue, set } from "firebase/database";

const TABLE_HEAD = [
  { id: "name", label: "Name", alignRight: false },
  { id: "email", label: "Email", alignRight: false },
  { id: "uid", label: "UID", alignRight: false },
  { id: "semester", label: "Semester", alignRight: false },
  { id: "dept", label: "Dept", alignRight: false },
  { id: "roll", label: "Roll", alignRight: false },
  { id: "status", label: "IsVerified", alignRight: false },
  { id: "" },
];

// ----------------------------------------------------------------------

function descendingComparator(a, b, orderBy) {
  if (b[orderBy] < a[orderBy]) {
    return -1;
  }
  if (b[orderBy] > a[orderBy]) {
    return 1;
  }
  return 0;
}

function getComparator(order, orderBy) {
  return order === "desc"
    ? (a, b) => descendingComparator(a, b, orderBy)
    : (a, b) => -descendingComparator(a, b, orderBy);
}

function applySortFilter(array, comparator, query) {
  const stabilizedThis = array.map((el, index) => [el, index]);
  stabilizedThis.sort((a, b) => {
    const order = comparator(a[0], b[0]);
    if (order !== 0) return order;
    return a[1] - b[1];
  });
  if (query) {
    return filter(
      array,
      (_user) => _user.name.toLowerCase().indexOf(query.toLowerCase()) !== -1
    );
  }
  return stabilizedThis.map((el) => el[0]);
}

export default function Users() {
  const [page, setPage] = useState(0);
  const [order, setOrder] = useState("asc");
  const [selected, setSelected] = useState([]);
  const [orderBy, setOrderBy] = useState("name");
  const [filterName, setFilterName] = useState("");
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [filteredUsers, setFilteredUsers] = useState([]);

  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const db = getDatabase();
    const userRef = ref(db, "users");
    const userList = [];

    onValue(
      userRef,
      (snapshot) => {
        snapshot.forEach((childSnapshot) => {
          const user = childSnapshot.val();
          user.id = childSnapshot.key;
          user.isVerified = "Yes"; // todo: remove this when implementing the backend api using firebase admin
          userList.push(user);
        });

        console.log(userList);
        setFilteredUsers(
          applySortFilter(userList, getComparator(order, orderBy), filterName)
        );
        setIsLoading(false);
      },
      {
        onlyOnce: true,
      }
    );

    // setFilteredUsers(
    //   applySortFilter(USERLIST, getComparator(order, orderBy), filterName)
    // );
  }, []);

  const handleFilterByName = (event) => {
    setFilterName(event.target.value);
    setFilteredUsers(
      applySortFilter(
        filteredUsers,
        getComparator(order, orderBy),
        event.target.value
      )
    );
  };

  const handleRequestSort = (event, property) => {
    const isAsc = orderBy === property && order === "asc";
    setOrder(isAsc ? "desc" : "asc");
    setOrderBy(property);
  };

  const handleSelectAllClick = (event) => {
    if (event.target.checked) {
      const newSelecteds = filteredUsers.map((n) => n.name);
      setSelected(newSelecteds);
      return;
    }
    setSelected([]);
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const onDeleteClick = (id) => {
    // TODO: implement it in the backend
    // console.log("Deleted!", id);
    // setFilteredUsers(filteredUsers.filter((user) => user.id !== id));
  };

  const onMakeVolunteerClick = (id) => {
    const db = getDatabase();
    set(ref(db, `volunteers/${id}`), true)
      .then(() => {
        console.log("data saved!");
      })
      .catch((err) => {
        console.trace("error when making volunteer!", err);
      });
  };

  const onEnableAccountClick = (id) => {
    // TODO: implement it in the backend
    // console.log("on enable click!", id);
    // setFilteredUsers(
    //   filteredUsers.map((user) => {
    //     if (user.id === id) {
    //       return {
    //         ...user,
    //         isVerified: "Yes",
    //       };
    //     }
    //     return user;
    //   })
    // );
  };

  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - filteredUsers.length) : 0;

  const isUserNotFound = filteredUsers.length === 0;

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <Appbar />
      <div style={{ marginTop: 20 }}></div>
      <Page title="User | Minimal-UI">
        <Container>
          <Stack
            direction="row"
            alignItems="center"
            justifyContent="space-between"
            mb={5}
          >
            <Typography variant="h4" gutterBottom>
              User
            </Typography>
            <Button
              variant="contained"
              component={RouterLink}
              to="#"
              startIcon={<Icon icon={plusFill} />}
            >
              New User
            </Button>
          </Stack>

          <Card>
            <UserListToolbar
              numSelected={selected.length}
              filterName={filterName}
              onFilterName={handleFilterByName}
            />
            <TableContainer sx={{ minWidth: 800 }}>
              <Table>
                <UserListHead
                  order={order}
                  orderBy={orderBy}
                  headLabel={TABLE_HEAD}
                  rowCount={filteredUsers.length}
                  numSelected={selected.length}
                  onRequestSort={handleRequestSort}
                  onSelectAllClick={handleSelectAllClick}
                />
                <TableBody>
                  {filteredUsers
                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                    .map((row) => {
                      const {
                        id,
                        name,
                        userImage,
                        email,
                        semester,
                        department,
                        universityId,
                        isVerified,
                      } = row;
                      const isItemSelected = selected.indexOf(name) !== -1;

                      return (
                        <TableRow
                          hover
                          key={id}
                          tabIndex={-1}
                          role="checkbox"
                          selected={isItemSelected}
                          aria-checked={isItemSelected}
                        >
                          <TableCell
                            component="th"
                            scope="row"
                            padding="normal"
                          >
                            <Stack
                              direction="row"
                              alignItems="center"
                              spacing={2}
                            >
                              <Avatar alt={name} src={userImage} />
                              <Typography variant="subtitle2" noWrap>
                                {name}
                              </Typography>
                            </Stack>
                          </TableCell>
                          <TableCell align="left">{email}</TableCell>
                          <TableCell align="left">{id}</TableCell>
                          <TableCell align="left">{semester}</TableCell>
                          <TableCell align="left">{department}</TableCell>
                          <TableCell align="left">{universityId}</TableCell>

                          <TableCell align="left">
                            <Label
                              variant="ghost"
                              color={
                                (isVerified === "No" && "error") || "success"
                              }
                            >
                              {sentenceCase(isVerified)}
                            </Label>
                          </TableCell>

                          <TableCell align="right">
                            <UserMoreMenu
                              onDeleteClick={onDeleteClick}
                              onMakeVolunteerClick={onMakeVolunteerClick}
                              onEnableAccountClick={onEnableAccountClick}
                              id={id}
                            />
                          </TableCell>
                        </TableRow>
                      );
                    })}
                  {emptyRows > 0 && (
                    <TableRow style={{ height: 53 * emptyRows }}>
                      <TableCell colSpan={6} />
                    </TableRow>
                  )}
                </TableBody>
                {isUserNotFound && (
                  <TableBody>
                    <TableRow>
                      <TableCell align="center" colSpan={6} sx={{ py: 3 }}>
                        <h>NOT FOUND</h>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                )}
              </Table>
            </TableContainer>

            <TablePagination
              rowsPerPageOptions={[5, 10, 25]}
              component="div"
              count={filteredUsers.length}
              rowsPerPage={rowsPerPage}
              page={page}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
            />
          </Card>
        </Container>
      </Page>
    </>
  );
}
