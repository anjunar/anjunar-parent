package de.industrialsociety.common.security;


import de.industrialsociety.common.filedisk.HarddiskFile;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "UserImage")
@Table(name = "ge_image")
public class UserImage extends HarddiskFile {}
