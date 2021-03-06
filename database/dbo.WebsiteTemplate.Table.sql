USE [homepage]
GO
/****** Object:  Table [dbo].[WebsiteTemplate]    Script Date: 4/20/2020 6:21:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WebsiteTemplate](
	[Website] [varchar](max) NULL,
	[DateUpdated] [varchar](max) NULL,
	[DatePosted] [varchar](max) NULL,
	[stringRemove] [varchar](max) NULL,
	[InternalId] [int] IDENTITY(1,1) NOT NULL,
	[CookieAndValue] [varchar](max) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Index [WebsiteTemplate_InternalId]    Script Date: 4/20/2020 6:21:21 PM ******/
CREATE CLUSTERED INDEX [WebsiteTemplate_InternalId] ON [dbo].[WebsiteTemplate]
(
	[InternalId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[WebsiteTemplate] ON 

SET IDENTITY_INSERT [dbo].[WebsiteTemplate] OFF
