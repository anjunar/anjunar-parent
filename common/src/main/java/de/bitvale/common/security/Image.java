package de.bitvale.common.security;


import de.bitvale.common.filedisk.HarddiskFile;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "UserImage")
@Table(name = "co_image")
public class Image extends HarddiskFile {}
